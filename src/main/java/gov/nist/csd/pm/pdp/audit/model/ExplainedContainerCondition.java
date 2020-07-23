package gov.nist.csd.pm.pdp.audit.model;

import gov.nist.csd.pm.pip.prohibitions.model.ContainerCondition;

public class ExplainedContainerCondition extends ContainerCondition {

    private boolean satisfied;

    public ExplainedContainerCondition(String name, boolean complement, boolean satisfied) {
        super(name, complement);

        this.satisfied = satisfied;
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }
}
