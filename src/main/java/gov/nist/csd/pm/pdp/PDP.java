package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.policies.SuperPolicy;
import gov.nist.csd.pm.pdp.services.*;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

import java.util.Set;

public class PDP {

    /**
     * Factory method to create a new PDP and EPP, while abstracting the initialization process. A PDP needs an EPP, and
     * vice versa. These steps need to occur each time a new PDP/EPP is created. The order of each step matters inorder to
     * avoid any race conditions. The EPP is accessible via the PDP's getEPP method.
     *
     * @return the new PDP instance
     */
    public static PDP newPDP(PAP pap, EPPOptions eppOptions, OperationSet resourceOps) throws PMException {
        // create PDP
        PDP pdp = new PDP(pap, resourceOps);
        // create the EPP
        EPP epp = new EPP(pap, pdp, eppOptions);
        // set the PDPs EPP
        pdp.setEPP(epp);
        // initialize PDP services which need the epp that was just set
        pdp.initServices();
        return pdp;
    }

    private PAP pap;
    private OperationSet resourceOps;

    private EPP epp;
    private GraphService graphService;
    private ProhibitionsService prohibitionsService;
    private AnalyticsService    analyticsService;
    private ObligationsService obligationsService;

    /**
     * Create a new PDP instance given a Policy Administration Point and an optional set of FunctionExecutors to be
     * used by the EPP.
     * @param pap the Policy Administration Point that the PDP will use to change the graph.
     * @param resourceOps the set of operations that the PDP will understand while traversing the graph.
     * @throws PMException if there is an error initializing the EPP.
     */

    public PDP(PAP pap, OperationSet resourceOps) throws PMException {
        this.pap = pap;
        this.resourceOps = resourceOps;
    }

    private void setEPP(EPP epp) {
        this.epp = epp;
    }

    public void initServices() throws PMException {
        // initialize services
        this.graphService = new GraphService(pap, this.epp, resourceOps);
        this.prohibitionsService = new ProhibitionsService(pap, this.epp, resourceOps);
        this.analyticsService = new AnalyticsService(pap, this.epp, resourceOps);
        this.obligationsService = new ObligationsService(pap, this.epp, resourceOps);
    }

    public OperationSet getResourceOps() {
        return resourceOps;
    }

    public void setResourceOps(OperationSet resourceOps) {
        this.graphService.setResourceOps(resourceOps);
        this.prohibitionsService.setResourceOps(resourceOps);
        this.analyticsService.setResourceOps(resourceOps);
        this.obligationsService.setResourceOps(resourceOps);
        this.resourceOps = resourceOps;
    }

    public void addResourceOps (String... ops) {
        OperationSet resourceOperations = getResourceOps();
        String[] newOps = ops;
        for (String op: newOps) {
            if (!resourceOperations.contains(op)) {
                resourceOperations.add(op);
            }
        }
        setResourceOps(resourceOperations);
    }

    public void deleteResourceOps (String... ops) {
        OperationSet resourceOperations = getResourceOps();
        String[] newOps = ops;
        for (String op: newOps) {
            if (resourceOperations.contains(op)) {
                resourceOperations.remove(op);
            }
        }
        setResourceOps(resourceOperations);
    }

    public EPP getEPP() {
        return epp;
    }

    public GraphService getGraphService (UserContext userCtx) {
        graphService.setUserCtx(userCtx);
        return graphService;
    }

    public Prohibitions getProhibitionsService (UserContext userCtx) {
        prohibitionsService.setUserCtx(userCtx);
        return prohibitionsService;
    }

    public AnalyticsService getAnalyticsService(UserContext userCtx) {
        analyticsService.setUserCtx(userCtx);
        return analyticsService;
    }

    public Obligations getObligationsService(UserContext userCtx) {
        obligationsService.setUserCtx(userCtx);
        return obligationsService;
    }
}
