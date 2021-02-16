package com.terminalvelocitycabbage.fontmaker;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FontInfo {

    int textureHeight;
    Map<Character, CharInfo> characterInfo;

    public FontInfo(int textureHeight, Map<Character, CharInfo> characters) {
        this.textureHeight = textureHeight;
        this.characterInfo = characters;
    }

    private FontInfo() {
        this.characterInfo = new HashMap<>();
    }

    public void write(String fileName) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(new FileOutputStream(fileName));
        dataOut.writeInt(textureHeight);
        for (Character character : characterInfo.keySet()) {
            dataOut.writeChar(character);
            dataOut.writeInt(characterInfo.get(character).getStartX());
            dataOut.writeInt(characterInfo.get(character).getWidth());
        }
        dataOut.flush();
    }

    public static FontInfo read(String fileName) throws IOException {
        FontInfo info = new FontInfo();
        DataInputStream dataIn = new DataInputStream(new FileInputStream(fileName));
        info.setHeight(dataIn.readInt());
        while (dataIn.available() > 0) {
            info.addChar(dataIn.readChar(), new CharInfo(dataIn.readInt(), dataIn.readInt()));
        }
        return info;
    }

    private void setHeight(int textureHeight) {
        this.textureHeight = textureHeight;
    }

    private void addChar(Character character, CharInfo charInfo) {
        this.characterInfo.put(character, charInfo);
    }
}
