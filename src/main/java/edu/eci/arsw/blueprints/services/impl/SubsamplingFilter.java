package edu.eci.arsw.blueprints.services.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.services.BlueprintFilter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * A filter that subsamples a blueprint by keeping every other point.
 */
@Component
public class SubsamplingFilter implements BlueprintFilter {

    /**
     * Filters a blueprint by keeping every other point.
     *
     * @param blueprint The blueprint to be filtered.
     * @return A new blueprint with a reduced number of points.
     */
    @Override
    public Blueprint filter(Blueprint blueprint) {
        List<Point> points = blueprint.getPoints();
        List<Point> filteredPoints = new ArrayList<>();

        for (int i = 0; i < points.size(); i += 2) {
            filteredPoints.add(points.get(i));
        }

        return new Blueprint(blueprint.getAuthor(), blueprint.getName(), filteredPoints.toArray(new Point[0]));
    }
}