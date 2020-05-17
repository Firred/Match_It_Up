package com.example.matchitup.game;

public interface GameFactoryInterface {

    /**
     * Abstract method that must return an Game with the appropriate characteristics of the Easy level
     * @param gameModeString
     * @param record
     * @return Game
     */
    Game easyGame(String gameModeString, int record);

    /**
     * Abstract method that must return an Game with the appropriate characteristics of the Medium level
     * @param gameModeString
     * @param record
     * @return Game
     */
    Game mediumGame(String gameModeString, int record);

    /**
     * Abstract method that must return an Game with the appropriate characteristics of the Hard level
     * @param gameModeString
     * @param record
     * @return Game
     */
    Game hardGame(String gameModeString, int record);

}
