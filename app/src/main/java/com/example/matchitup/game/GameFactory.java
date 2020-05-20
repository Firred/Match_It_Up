package com.example.matchitup.game;

public class GameFactory implements GameFactoryInterface {

    /**
     * Method that must return an Game with the appropriate characteristics of the Easy level
     * @param gameModeString
     * @param record
     * @return Game
     */
    @Override
    public Game easyGame(String gameModeString, int record) {
        return new EasyGame(gameModeString, record, 3, 12000, -1 );
    }

    /**
     * Method that must return an Game with the appropriate characteristics of the Medium level
     * @param gameModeString
     * @param record
     * @return Game
     */
    @Override
    public Game mediumGame(String gameModeString, int record) {
        return new MediumGame(gameModeString, record, 4, 5000, 12000);
    }

    /**
     * Method that must return an Game with the appropriate characteristics of the Hard level
     * @param gameModeString
     * @param record
     * @return Game
     */
    @Override
    public Game hardGame(String gameModeString, int record) {
        return new HardGame(gameModeString, record, 5, 0, 5000);
    }
}
