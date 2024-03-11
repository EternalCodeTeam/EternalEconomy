package com.eternalcode.eternaleconomy.viewer;

import com.eternalcode.eternaleconomy.user.User;

import java.util.Collection;
import java.util.UUID;

public interface ViewerService {

    Collection<Viewer> all();

    Collection<Viewer> onlinePlayers();

    Viewer console();

    Viewer player(UUID uuid);

    Viewer user(User user);

    Viewer any(Object any);
}
