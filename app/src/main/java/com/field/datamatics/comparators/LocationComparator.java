package com.field.datamatics.comparators;

import com.field.datamatics.database.JoinClientRoutePlan;

import java.util.Comparator;

/**
 * Created by Anoop on 20-09-2016.
 */
public class LocationComparator implements  Comparator<JoinClientRoutePlan> {
    @Override
    public int compare(JoinClientRoutePlan lhs, JoinClientRoutePlan rhs) {
        return lhs.Location.compareTo(rhs.Location);
    }
}
