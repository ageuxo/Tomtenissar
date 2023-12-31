package io.github.ageuxo.TomteMod.entity;

public interface MoodyMob {
    int getMood();

    void setMood(int mood);

    void addMood(int mood, boolean visible);
}
