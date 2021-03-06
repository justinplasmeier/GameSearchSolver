package com.jplaz.eecs391;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import static com.jplaz.eecs391.RubiksMove.*;

public class RubiksPuzzleState implements GameState {

    private final static int BOARD_SIZE = 2*2*6;

    private final static short GOAL_STATE[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23};

    private short gameBoard[];

    // initialize to max int so the initial cost comparison is always chosen
    private int pathCost = Integer.MAX_VALUE;

    private LinkedList<String> pathToNode = new LinkedList<>();

    public RubiksPuzzleState(short[] initialBoard) {
        this.gameBoard = new short[GOAL_STATE.length];
        System.arraycopy(initialBoard, 0, this.gameBoard, 0, initialBoard.length);
    }

    public RubiksPuzzleState() {
        this(GOAL_STATE);
    }

    public int getPathCost() {
        return this.pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public LinkedList getPathToNode() {
        return (LinkedList) this.pathToNode.clone();
    }

    public void setPathToNode(LinkedList newPath) {
        this.pathToNode = newPath;
    }
    public void setState(String state) {
        // string representation of goal state is:
        // "0123 4567 89ab cdef ghij klmn"
        // this uses "Triacontakaidecimal" numbering
        short newBoard[] = new short[BOARD_SIZE];
        int boardPosition = 0;

        System.out.println();

        for(int i = 0; i < BOARD_SIZE; i++) {
            if (!(state.charAt(i) == ' ')) {
                newBoard[boardPosition] = (short) Character.getNumericValue(state.charAt(i));
                System.out.print(" " + newBoard[boardPosition]);
                boardPosition++;
            }
        }
        this.gameBoard = newBoard;
    }

    public void setToGoalState() {
        System.arraycopy(GOAL_STATE, 0 , this.gameBoard, 0, GOAL_STATE.length);
    }

    public void appendMoveToPath(String moveString) {
        this.pathToNode.add(moveString);
    }

    public RubiksPuzzleState randomizeState(int n) {
        Random rand = new Random(391L);
        System.arraycopy(GOAL_STATE, 0, this.gameBoard, 0, GOAL_STATE.length);
        RubiksPuzzleState newState = this;

        for (int i = 0; i < n; i++) {
            newState = newState.makeRandomMove(rand);
        }
        return newState;
    }

    // copied from NPuzzleState - can abstract out
    private RubiksPuzzleState makeRandomMove(Random rand) {
        ArrayList<Move> validMoves = this.getValidMoves();
        Move randomMove = validMoves.get(rand.nextInt(validMoves.size()));
        System.out.println("Making random move: " + randomMove.toString());
        return move(randomMove);
    }

    public RubiksPuzzleState move(Move move) {
        RubiksMove rMove = (RubiksMove) move;
        if (!isValidMove(rMove)) {
            System.out.println("Invalid RubiksMove! Try again.");
            return this;
        }
        short newBoard[] = new short[BOARD_SIZE];
        System.arraycopy(this.gameBoard, 0, newBoard, 0, BOARD_SIZE);

        // in theory this can be generalized
        // I just have no idea how to do that
        // the ordering is clockwise
        // so CW move has 12 replace 13 replace 14 replace 15 replace 12
        switch (rMove) {
            case FRONT_CW:
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 12, 13, 14, 15);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 3, 11, 19, 23);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 2, 10, 18, 22);
                return new RubiksPuzzleState(newBoard);
            case BACK_CW:
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 4, 5, 6, 7);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 1, 21, 17, 9);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 0, 20, 16, 8);
                return new RubiksPuzzleState(newBoard);
            case LEFT_CW:
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 0, 1, 2, 3);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 4, 8, 12, 22);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 7, 11, 15, 21);
                return new RubiksPuzzleState(newBoard);
            case RIGHT_CW:
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 16, 17, 18, 19);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 6, 20, 14, 10);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 5, 23, 13, 9);
                return new RubiksPuzzleState(newBoard);
            case TOP_CW:
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 8, 9, 10, 11);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 2, 7, 13, 16);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 1, 6, 19, 12);
                return new RubiksPuzzleState(newBoard);
            case BOTTOM_CW:
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 20, 21, 22, 23);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 3, 14, 17, 4);
                newBoard = shiftPositions(newBoard, Direction.CLOCKWISE, 0, 15, 18, 5);
                return new RubiksPuzzleState(newBoard);
            case FRONT_CCW:
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 12, 13, 14, 15);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 3, 11, 19, 23);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 2, 10, 18, 22);
                return new RubiksPuzzleState(newBoard);
            case BACK_CCW:
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 4, 5, 6, 7);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 1, 21, 17, 9);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 0, 20, 16, 8);
                return new RubiksPuzzleState(newBoard);
            case LEFT_CCW:
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 0, 1, 2, 3);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 4, 8, 12, 22);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 7, 11, 15, 21);
                return new RubiksPuzzleState(newBoard);
            case RIGHT_CCW:
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 16, 17, 18, 19);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 6, 20, 14, 10);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 5, 23, 13, 9);
                return new RubiksPuzzleState(newBoard);
            case TOP_CCW:
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 8, 9, 10, 11);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 2, 7, 13, 16);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 1, 6, 19, 12);
                return new RubiksPuzzleState(newBoard);
            case BOTTOM_CCW:
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 20, 21, 22, 23);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 3, 14, 17, 4);
                newBoard = shiftPositions(newBoard, Direction.COUNTERCLOCKWISE, 0, 15, 18, 5);
                return new RubiksPuzzleState(newBoard);
            default:
                return this;
        }
    }

    /*
     * move the given positions, expecting a clockwise ordering
     */
    private short[] shiftPositions(short[] gameBoard, Direction direction, int a, int b, int c, int d) {
        short tmp = gameBoard[a];
        switch (direction) {
            case CLOCKWISE:
                gameBoard[a] = gameBoard[d];
                gameBoard[d] = gameBoard[c];
                gameBoard[c] = gameBoard[b];
                gameBoard[b] = tmp;
                return gameBoard;
            case COUNTERCLOCKWISE:
                gameBoard[a] = gameBoard[b];
                gameBoard[b] = gameBoard[c];
                gameBoard[c] = gameBoard[d];
                gameBoard[d] = tmp;
                return gameBoard;
            default:
                System.out.println("Invalid RubiksMove direction.");
                return gameBoard;
        }
    }

    public int calculateHeuristic(String heuristic) {
        switch (heuristic) {
            case "h1":
                return numberOfMisplacedTiles();
            default:
                return 0;
        }
    }

    public ArrayList<Move> getValidMoves() {
        // unlike the 8-puzzle, all moves are valid
        return new ArrayList<>(Arrays.asList(RubiksMove.values()));
    }

    private boolean isValidMove(RubiksMove move) {
        // unlike the 8-puzzle, all moves are valid
        return true;
    }

    public RubiksMove stringToMove(String s) {
        switch (s) {
            case "top_ccw":
                return TOP_CCW;
            case "front_ccw":
                return FRONT_CCW;
            case "left_ccw":
                return LEFT_CCW;
            case "right_ccw":
                return RIGHT_CCW;
            case "bottom_ccw":
                return BOTTOM_CCW;
            case "back_ccw":
                return BACK_CCW;
            case "top_cw":
                return TOP_CW;
            case "front_cw":
                return FRONT_CW;
            case "left_cw":
                return LEFT_CW;
            case "right_cw":
                return RIGHT_CW;
            case "bottom_cw":
                return BOTTOM_CW;
            case "back_cw":
                return BACK_CW;
        }
        System.out.println("WARNING! Illegal move specified for Rubiks Cube.");
        return null;
    }

    public boolean isGoalState() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (GOAL_STATE[i] != this.gameBoard[i]) {
                return false;
            }
        }
        return true;
    }

    // Heuristics

    /*
     * Heuristic h1
     */
    public int numberOfMisplacedTiles() {
        int numberOfTiles = 0;
        for (int i = 0; i < gameBoard.length; i++) {
            if (gameBoard[i] != GOAL_STATE[i]) {
                numberOfTiles++;
            }
        }
        return numberOfTiles;
    }


    public void printState() {
        System.out.print("\"");
        for (int i = 0; i < this.gameBoard.length; i++) {
            if (i % 4 == 0 && i != 0) {
                System.out.print(" ");
            }
            if (i >= 10) {
                System.out.print((char) (i+87));
            }
            else {
                System.out.print(gameBoard[i]);
            }
        }
        System.out.print("\"");
        System.out.println();
    }

    public void printPath() {
        for (String moveString : this.pathToNode) {
            System.out.print(moveString + " ");
        }
        System.out.println();
    }

    // Overridden methods

    @Override
    public boolean equals(Object o2) {
        if (!(o2 instanceof RubiksPuzzleState)) {
            return false;
        }
        RubiksPuzzleState n2 = (RubiksPuzzleState) o2;

        for (int i = 0; i < this.gameBoard.length; i++) {
            if (this.gameBoard[i] != n2.gameBoard[i]) {
                return false;
            }
        }
        return true;

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.gameBoard);
    }
}
