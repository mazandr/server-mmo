package com.win.strategy.battle.utils;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import java.util.List;

public class BMPhysicsUtils {
//
//    public static boolean los(int x0, int y0, int x1, int y1, int[][] shadowMap) {
//
//        int sx, sy;
//        int dx = x1 - x0;
//        int dy = y1 - y0;
//        if (x0 < x1) {
//            sx = 1;
//        } else {
//            sx = -1;
//        }
//        if (y0 < y1) {
//            sy = 1;
//        } else {
//            sy = -1;
//        }
//        // sx and sy are switches that enable us to compute the LOS in a single
//        // quarter of x/y plan
//        int xnext = x0;
//        int ynext = y0;
//        double denom = Math.sqrt(dx * dx + dy * dy);
//        while (xnext != x1 || ynext != y1) {
//            // check map bounds here if needed
//            if (shadowMap[xnext][ynext] == 1) {
//
//                return false;
//            }
//            // Line-to-point distance formula < 0.5
//            if (Math.abs(dy * (xnext - x0 + sx) - dx * (ynext - y0)) / denom < 0.5f) {
//                xnext += sx;
//            } else if (Math.abs(dy * (xnext - x0) - dx * (ynext - y0 + sy))
//                    / denom < 0.5f) {
//                ynext += sy;
//            } else {
//                xnext += sx;
//                ynext += sy;
//            }
//        }
//        return true;
//    }

    public static boolean los(BMCell from, BMCell to, List<BMCell> tmpPoints, BMEntity entity) {

        BMField field = from.getField();
        int sx, sy;

        int x0 = from.getPosX();
        int x1 = to.getPosX();
        int y0 = from.getPosY();
        int y1 = to.getPosY();

        int dx = x1 - x0;
        int dy = y1 - y0;
        if (x0 < x1) {
            sx = 1;
        } else {
            sx = -1;
        }
        if (y0 < y1) {
            sy = 1;
        } else {
            sy = -1;
        }
        // sx and sy are switches that enable us to compute the LOS in a single
        // quarter of x/y plan
        int xnext = x0;
        int ynext = y0;
        double denom = Math.sqrt(dx * dx + dy * dy);
        while (xnext != x1 || ynext != y1) {
            // check map bounds here if needed
            BMCell cell = field.getCell(xnext, ynext);
            if (cell.getBuilding() != null || cell.getTrash() != null) {
                return false;
            }
            // Line-to-point distance formula < 0.5
            if (Math.abs(dy * (xnext - x0 + sx) - dx * (ynext - y0)) / denom < 0.5f) {
                xnext += sx;
            } else if (Math.abs(dy * (xnext - x0) - dx * (ynext - y0 + sy))
                    / denom < 0.5f) {
                ynext += sy;
            } else {
                xnext += sx;
                ynext += sy;
            }

            tmpPoints.add(field.getCell(xnext, ynext));
        }
        return true;

    }
}
