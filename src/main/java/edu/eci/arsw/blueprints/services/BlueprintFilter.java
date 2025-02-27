package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.model.Blueprint;

/**
 * Interface for applying a filter to a blueprint.
 */
public interface BlueprintFilter {
    /**
     * Applies a filtering mechanism to the given blueprint.
     *
     * @param blueprint The blueprint to be filtered.
     * @return A new filtered blueprint.
     */
    public Blueprint filter(Blueprint blueprint);
}
