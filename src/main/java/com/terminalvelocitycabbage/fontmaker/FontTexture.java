package com.terminalvelocitycabbage.fontmaker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;

public class FontTexture {

    public static final String IMAGE_FORMAT = "png";
    public static final String DEFAULT_CHARSET = "ISO-8859-1";
    public static final int CHAR_PADDING = 2;
    public static final float DEFAULT_SIZE = 32f;

    private Font font;
    private String charSetName;
    private Map<Character, CharInfo> charMap;

    private int height;
    private int position;

    private BufferedImage image;

    public FontTexture(String fileName, String charSet, float size) {
        File file = new File(fileName);
        try {
            InputStream is = new FileInputStream(file);
            this.font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            this.charSetName = charSet;
            charMap = new HashMap<>();
            buildTexture();
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getAllAvailableChars(String charsetName) {
        CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
        StringBuilder result = new StringBuilder();
        for (char c = 0; c < Character.MAX_VALUE; c++) {
            if (ce.canEncode(c)) {
                result.append(c);
            }
        }
        return result.toString();
    }

    private void buildTexture() throws IOException {
        // Get the font metrics for each character for the selected font by using image
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        FontMetrics fontMetrics = g2D.getFontMetrics();

        String allChars = getAllAvailableChars(charSetName);
        this.position = 0;
        this.height = fontMetrics.getHeight();
        for (char c : allChars.toCharArray()) {
            // Get the size for each character and update global image size
            CharInfo charInfo = new CharInfo(position, fontMetrics.charWidth(c));
            charMap.put(c, charInfo);
            position += charInfo.getWidth() + CHAR_PADDING;
        }
        g2D.dispose();

        // Create the image associated to the charset
        img = new BufferedImage(position, height, BufferedImage.TYPE_4BYTE_ABGR);
        g2D = img.createGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setFont(font);
        fontMetrics = g2D.getFontMetrics();
        g2D.setColor(Color.WHITE);
        int startX = 0;
        for (char c : allChars.toCharArray()) {
            CharInfo charInfo = charMap.get(c);
            g2D.drawString("" + c, startX, fontMetrics.getAscent());
            startX += charInfo.getWidth() + CHAR_PADDING;
        }
        g2D.dispose();

        try ( ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(img, IMAGE_FORMAT, out);
            out.flush();
        }
        image = img;
    }

    public Map<Character, CharInfo> getCharMap() {
        return charMap;
    }

    public int getHeight() {
        return height;
    }

    public void writeImageToFile(String fileName) throws IOException {
        ImageIO.write(image, IMAGE_FORMAT, new File(fileName + ".png"));
    }

    public void writeFontInfo(String fileName) throws IOException {
        new FontInfo(getHeight(), getCharMap()).write( fileName + ".tvfont");
    }
}
