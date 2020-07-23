package gov.nist.csd.pm.pdp.audit;

import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.audit.model.*;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

class PReviewAuditorTest {

    @Test
    void testExplain() throws PMException {
        for(TestCases.TestCase tc : TestCases.getTests()) {
            PReviewAuditor auditor = new PReviewAuditor(tc.graph, new OperationSet("read", "write", "execute"));
            Explain explain = auditor.explain("u1", "o1");

            assertTrue(explain.getPermissions().containsAll(tc.getExpectedOps()),
                    tc.name + " expected ops " + tc.getExpectedOps() + " but got " + explain.getPermissions());

            for (String pcName : tc.expectedPaths.keySet()) {
                List<String> expectedPaths = tc.expectedPaths.get(pcName);
                assertNotNull(expectedPaths, tc.name);

                PolicyClass pc = explain.getPolicyClasses().get(pcName);
                assertEquals(expectedPaths.size(), pc.getPaths().size(), tc.name);
                for (String exPathStr : expectedPaths) {
                    boolean match = false;
                    for (Path resPath : pc.getPaths()) {
                        if(pathsMatch(exPathStr, resPath.toString())) {
                            match = true;
                            break;
                        }
                    }
                    assertTrue(match, tc.name + " expected path \"" + exPathStr + "\" but it was not in the results \"" + pc.getPaths() + "\"");
                }
            }
        }
    }

    private boolean pathsMatch(String expectedStr, String actualStr) {
        String[] expectedArr = expectedStr.split("-");
        String[] actualArr = actualStr.split("-");

        if (expectedArr.length != actualArr.length) {
            return false;
        }

        for (int i = 0; i < expectedArr.length; i++) {
            String ex = expectedArr[i];
            String res = actualArr[i];
            // if the element has brackets, it's a list of permissions
            if (ex.startsWith("[") && res.startsWith("[")) {
                // trim the brackets from the strings
                ex = ex.substring(1, ex.length()-1);
                res = res.substring(1, res.length()-1);

                // split both into an array of strings
                String[] exOps = ex.split(",");
                String[] resOps = res.split(",");

                Arrays.sort(exOps);
                Arrays.sort(resOps);

                if (exOps.length != resOps.length) {
                    return false;
                }
                for (int j = 0; j < exOps.length; j++) {
                    if (!exOps[j].equals(resOps[j])) {
                        return false;
                    }
                }
            } else if (!ex.equals(actualArr[i])) {
                return false;
            }
        }

        return true;
    }

    @Test
    void testProhibitionsAudit() throws PMException {
        Graph graph = new MemGraph();
        graph.createPolicyClass("pc1", null);
        graph.createNode("oa1", OA, null, "pc1");
        graph.createNode("ua1", UA, null, "pc1");
        graph.createNode("o1", O, null, "oa1");
        graph.createNode("u1", U, null, "ua1");

        graph.associate("ua1", "oa1", new OperationSet("read", "write"));

        Prohibitions prohibitions = new MemProhibitions();
        prohibitions.add(new Prohibition.Builder("test", "ua1", new OperationSet("write"))
                .addContainer("oa1", false)
                .build());

        PReviewAuditor auditor = new PReviewAuditor(graph, prohibitions, new OperationSet("read", "write"));
        Explain explain = auditor.explain("u1", "o1");
        ExplainedProhibitions explainProhibitions = explain.getProhibitions();
        assertEquals(new OperationSet("write"), explainProhibitions.getOperations());

        List<ExplainedProhibition> prohibitionsList = explainProhibitions.getProhibitions();
        assertEquals(1, prohibitionsList.size());

        ExplainedProhibition explainedProhibition = prohibitionsList.get(0);
        Set<ExplainedContainerCondition> containerConditions = explainedProhibition.getContainerConditions();
        assertEquals(1, containerConditions.size());

        ExplainedContainerCondition ecc = containerConditions.iterator().next();
        assertTrue(ecc.isSatisfied());
    }
}