        LOC     0           ;EXPECTED: GPR[3]=3 at HLT (3 sub-tests passed)
        Data    1           ;addr 0: positive
        Data    65535       ;addr 1: negative (-1)

        LOC     10
        LDR     0,0,0       ;R0 <- 1 (positive)
        JGE     0,0,JGE_PASS ;R0>=0 must branch
        HLT                  ;FAIL: JGE did not branch on positive
JGE_PASS: AIR   3,1          ;PASS GPR[3]=1

        LDR     0,0,1       ;R0 <- -1 (negative)
        JGE     0,0,JGE2_FAIL ;must NOT branch
        AIR     3,1          ;PASS GPR[3]=2
        JMA     0,JMA_TEST
JGE2_FAIL: HLT              ;FAIL: JGE branched on negative

JMA_TEST: JMA   0,JMA_PASS  ;must always jump
        HLT                  ;FAIL: JMA did not jump
JMA_PASS: AIR   3,1          ;PASS GPR[3]=3
        HLT                  ;ALL PASSED