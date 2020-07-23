package gov.nist.csd.pm.pdp.audit.model;

import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.prohibitions.model.ContainerCondition;

import java.util.List;
import java.util.Set;

public class ExplainedProhibition {

    private String subject;
    private OperationSet operations;
    private boolean intersection;
    private Set<ExplainedContainerCondition> containerConditions;

    public ExplainedProhibition() {

    }

    public ExplainedProhibition(String subject, OperationSet operations, boolean intersection, Set<ExplainedContainerCondition> containerConditions) {
        this.subject = subject;
        this.operations = operations;
        this.intersection = intersection;
        this.containerConditions = containerConditions;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public OperationSet getOperations() {
        return operations;
    }

    public void setOperations(OperationSet operations) {
        this.operations = operations;
    }

    public boolean isIntersection() {
        return intersection;
    }

    public void setIntersection(boolean intersection) {
        this.intersection = intersection;
    }

    public Set<ExplainedContainerCondition> getContainerConditions() {
        return containerConditions;
    }

    public void setContainerConditions(Set<ExplainedContainerCondition> containerConditions) {
        this.containerConditions = containerConditions;
    }
}
