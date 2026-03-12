        LOC     0           ;EXPECTED: GPR[3]=4 at HLT (4 sub-tests passed)
        Data    0           ;addr 0: zero
        Data    1           ;addr 1: positive

        LOC     10
        LDR     0,0,0       ;R0 <- 0
        JZ      0,0,JZ_PASS ;R0==0 must branch
        HLT                  ;FAIL: JZ did not branch on zero
JZ_PASS: AIR    3,1          ;PASS GPR[3]=1

        LDR     0,0,1       ;R0 <- 1
        JZ      0,0,JZ2_FAIL ;must NOT branch
        AIR     3,1          ;PASS GPR[3]=2
        JMA     0,JNE_TEST
JZ2_FAIL: HLT               ;FAIL: JZ branched on nonzero

JNE_TEST: JNE   0,0,JNE_PASS ;R0=1 must branch
        HLT                   ;FAIL: JNE did not branch on nonzero
JNE_PASS: AIR   3,1           ;PASS GPR[3]=3

        LDR     0,0,0        ;R0 <- 0
        JNE     0,0,JNE2_FAIL ;must NOT branch
        AIR     3,1           ;PASS GPR[3]=4
        HLT                   ;ALL PASSED

JNE2_FAIL: HLT               ;FAIL: JNE branched on zero