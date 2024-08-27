package com.eternalcode.economy;

public class EternalEconomyApiProvider {

    private static EternalEconomyApi eternalEconomyApi;

    public static EternalEconomyApi provide() {
        if (eternalEconomyApi == null) {
            throw new IllegalStateException("LobbyHeadsApi has not been initialized yet!");
        }

        return eternalEconomyApi;
    }

    static void initialize(EternalEconomyApi eternalEconomyApi) {
        if (EternalEconomyApiProvider.eternalEconomyApi != null) {
            throw new IllegalStateException("EternalEconomyApi has already been initialized!");
        }

        EternalEconomyApiProvider.eternalEconomyApi = eternalEconomyApi;
    }

    static void deinitialize() {
        if (eternalEconomyApi == null) {
            throw new IllegalStateException("EternalEconomyApi has not been initialized yet!");
        }

        eternalEconomyApi = null;
    }
}