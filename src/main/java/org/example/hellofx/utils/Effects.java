package org.example.hellofx.utils;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.Glow;

public class Effects {
    public static Glow glowEffect;
    public static Bloom bloomEffect;

    Effects() {
        glowEffect = new Glow(0.5);
        bloomEffect = new Bloom();
        bloomEffect.setThreshold(0.5);
    }
}
