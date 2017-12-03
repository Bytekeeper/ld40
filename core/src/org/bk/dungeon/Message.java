package org.bk.dungeon;

import com.badlogic.gdx.graphics.Color;

public class Message {
    public final Color color;
    public float remaining;
    public final String text;
    public boolean isNew = true;

    public Message(String text, float time) {
        this(text, time, Color.WHITE);
    }

    public Message(String text, float time, Color color) {
        this.text = text;
        this.color = color;
        remaining = time;
    }
}
