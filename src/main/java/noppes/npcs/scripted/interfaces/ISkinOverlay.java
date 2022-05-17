package noppes.npcs.scripted.interfaces;

public interface ISkinOverlay {
    void setTexture(String texture);

    String getTexture();

    void setGlow(boolean glow);

    boolean getGlow();

    void setAlpha(float alpha);
    float getAlpha();

    void setSize(float size);
    float getSize();

    void setScale(float scaleX, float scaleY);
    float getScaleX();
    float getScaleY();

    void setSpeed(float speedX, float speedY);
    float getSpeedX();
    float getSpeedY();
}
