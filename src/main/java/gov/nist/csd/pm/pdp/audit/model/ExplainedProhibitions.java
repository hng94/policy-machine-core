package gov.nist.csd.pm.pdp.audit.model;

import gov.nist.csd.pm.operations.OperationSet;

import java.util.List;

public class ExplainedProhibitions {

    private OperationSet operations;
    private List<ExplainedProhibition> prohibitions;

    public ExplainedProhibitions() {

    }

    public ExplainedProhibitions(OperationSet operations, List<ExplainedProhibition> prohibitions) {
        this.operations = operations;
        this.prohibitions = prohibitions;
    }

    public OperationSet getOperations() {
        return operations;
    }

    public void setOperations(OperationSet operations) {
        this.operations = operations;
    }

    public List<ExplainedProhibition> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<ExplainedProhibition> prohibitions) {
        this.prohibitions = prohibitions;
    }
}
