package com.gloomgarden;

public class Pest {
    private int health;
    private int maxHealth;

    public Pest(int health) {
        this.health = health;
        this.maxHealth = health;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public boolean isDead() {
        return health <= 0;
    }
}
