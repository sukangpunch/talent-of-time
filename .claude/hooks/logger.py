import json
import sys
import os
import datetime

def get_timestamp():
    return datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")

def write_log(log_path, lines):
    with open(log_path, "a", encoding="utf-8") as f:
        f.write("\n".join(lines) + "\n")

data = json.load(sys.stdin)
event_type = sys.argv[1] if len(sys.argv) > 1 else "UNKNOWN"
log_path = ".claude/activity.log"

tool_name = data.get("tool_name", "")
tool_input = data.get("tool_input", {})
timestamp = get_timestamp()

# 이벤트별 로그 포맷
if event_type == "PRE":
    command = tool_input.get("command", "")
    file_path = tool_input.get("file_path", "")
    description = tool_input.get("description", "")

    lines = [
        f"[{timestamp}] ▶ TOOL START : {tool_name}",
    ]
    if command:
        lines.append(f"  명령어  : {command}")
    if file_path:
        lines.append(f"  파일    : {os.path.basename(file_path)}")
    if description:
        lines.append(f"  설명    : {description}")

elif event_type == "POST":
    tool_response = data.get("tool_response", {})
    output = tool_response.get("output", "")[:200]  # 너무 길면 자름
    interrupted = tool_response.get("interrupted", False)

    lines = [
        f"[{timestamp}] ✓ TOOL END   : {tool_name}",
    ]
    if interrupted:
        lines.append(f"  상태    : 중단됨")
    if output:
        lines.append(f"  결과    : {output.strip()[:100]}")

elif event_type == "HOOK":
    hook_name = sys.argv[2] if len(sys.argv) > 2 else "unknown"
    lines = [
        f"[{timestamp}] ⚙ HOOK       : {hook_name}",
        f"  대상    : {tool_input.get('file_path', tool_input.get('command', ''))}"
    ]

elif event_type == "STOP":
    lines = [
        f"[{timestamp}] ■ SESSION END",
        f"  {'─' * 50}",
    ]

else:
    sys.exit(0)

write_log(log_path, lines)
