package gov.nist.csd.pm.pdp.decider.prohibitions;

import gov.nist.csd.pm.pip.prohibitions.model.ContainerCondition;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

import java.util.Map;
import java.util.Set;

public class ProhibitionsDecider {

    public static boolean areContainerConditionsSatisfied(Prohibition prohibition, Set<String> reached, String target) {
        boolean isIntersection = prohibition.isIntersection();

        Set<ContainerCondition> containers = prohibition.getContainers();
        for (ContainerCondition container : containers) {
            boolean isReached = reached.contains(container.getName());
            boolean satisfied = isContainerConditionSatisfied(target, prohibition.isIntersection(), container, isReached);

            // if the prohibition is not intersection, one satisfied container condition means
            // the prohibition is satisfied
            if (satisfied && !isIntersection) {
                return true;
            }

            // if the prohibition is the intersection, one unsatisfied container condition means the whole
            // prohibition is not satisfied
            if (!satisfied && isIntersection) {
                return false;
            }
        }

        return isIntersection;
    }

    public static boolean isContainerConditionSatisfied(String target, boolean intersection,
                                                        ContainerCondition container, boolean reached) {
        if (target.equals(container.getName()) && intersection) {
            return false;
        }

        return container.isComplement() != reached;
    }

}
