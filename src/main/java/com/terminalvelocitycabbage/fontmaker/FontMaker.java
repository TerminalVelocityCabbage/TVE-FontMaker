package com.terminalvelocitycabbage.fontmaker;

import java.io.IOException;
import java.util.Scanner;

public class FontMaker {

    public static void main(String[] args) {

        //Request the file name
        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the name of the file you want to convert");
        String fileName = scanner.nextLine();
        scanner.close();

        //Check that it's a true type font
        if (!fileName.endsWith(".ttf")) {
            throw new RuntimeException("Font Maker can only convert true type fonts (.ttf) files.");
        }

        //Get the true type font from the file
        FontTexture font = new FontTexture(fileName, FontTexture.DEFAULT_CHARSET, FontTexture.DEFAULT_SIZE);

        //Read the fontTexture to test that it's converted and ready to write
        System.out.println("READING FROM CONVERTED INFO");
        for (CharInfo charInfo : font.getCharMap().values()) {
            System.out.println(charInfo);
        }

        //Write the generated font files
        System.out.println("WRITING TO TVFONT TYPE");
        try {
            font.writeImageToFile(fileName.split("\\.")[0]);
            font.writeFontInfo(fileName.split("\\.")[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Read the printed front to test
        System.out.println("READING FROM GENERATED FILES");
        try {
            FontInfo fontInfo = FontInfo.read(fileName.split("\\.")[0] + ".tvfont");
            System.out.println(fontInfo.textureHeight);
            for (Character charInfo : fontInfo.characterInfo.keySet()) {
                System.out.println(charInfo + " : " + fontInfo.characterInfo.get(charInfo));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Done
        System.out.println("DONE");
    }

}
