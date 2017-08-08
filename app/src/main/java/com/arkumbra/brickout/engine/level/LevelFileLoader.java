package com.arkumbra.brickout.engine.level;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;

import com.arkumbra.brickout.engine.entity.Brick;
import com.arkumbra.brickout.engine.entity.BrickType;
import com.arkumbra.brickout.engine.entity.Position;
import com.arkumbra.brickout.engine.entity.StandardBrick;
import com.arkumbra.brickout.engine.entity.UnitSize;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lukegardener on 2017/07/29.
 */

public class LevelFileLoader {
    private static final String LEVELS_DIRECTORY = "levels/";


    public LoadingFileDetails loadLevel(Context context, String levelFileName) {
        List<String> fileLines = loadFileIntoLines(context, levelFileName);

        LoadingFileDetails fileDetails = parseLevelData(fileLines);

        return fileDetails;
    }

    private List<String> loadFileIntoLines(Context context, String levelFileName) {
        List<String> lines = new ArrayList<>();

//        try (BufferedReader br = new BufferedReader(new FileReader(LEVELS_DIRECTORY + levelFileName))) {
        try  {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(LEVELS_DIRECTORY + levelFileName)));
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
                lines.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }


    /* Sorry - I don't know how to load files nicely */
    private LoadingFileDetails parseLevelData(List<String> fileLines) {
        LoadingFileDetails fileDetails = new LoadingFileDetails();

        int lineCount = -1;
        for (String line : fileLines) {
            lineCount += 1;

            if (lineCount == 0) {
                fileDetails.setLevelUnitsAcross(Float.valueOf(line));

            } else if (lineCount == 1) {
                fileDetails.setLevelUnitsTall(Float.valueOf(line));

            } else {
                // Top two lines of the file are the width and height, so remove those from count
                int actualMapRowNumber = lineCount - 2;
                List<Brick> bricksOnLine = parseLineIntoBricks(actualMapRowNumber, line);
                fileDetails.getBricks().addAll(bricksOnLine);
            }
        }

        return fileDetails;
    }

    private List<Brick> parseLineIntoBricks(int rowNumber, String line) {
        List<Brick> bricks = new ArrayList<>();

        // Bricks are two tiles across, so skip one space if we find the beginning of a brick
        boolean foundStartOfBrickLastChar = false;
        int xColumn = -1;

        for (char c : line.toCharArray()) {
            xColumn += 1;

            if (foundStartOfBrickLastChar) {
                foundStartOfBrickLastChar = false;
                continue;
            }

            BrickType brickType = BrickType.getByLevelIcon(c);
            if (brickType == null) {
                foundStartOfBrickLastChar = false;
                continue;
            }

            Position pos = new Position(xColumn, rowNumber);
            Brick brick = new StandardBrick(brickType, pos);
            bricks.add(brick);
            foundStartOfBrickLastChar = true;
        }

        return bricks;
    }


}

