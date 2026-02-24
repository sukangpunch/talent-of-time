package com.example.talentoftime.auth.util;

import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RedirectHostConst {
    public static final Map<String, String> DEST_BASE = Map.of(
            "prod",  "https://sdij-crew.vercel.app/",
            "local", "http://localhost:5173"
    );
}
