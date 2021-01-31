package com.groep6.pfor.util;

import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;

/**
 * A listener that can be registered to an {@link Observable}.
 *
 * @author Owen Elderbroek
 */
public interface IObserver {
    /** A callback method that will be called upon state change. */
    void update() throws CouldNotFindLocalPlayerException;
}
