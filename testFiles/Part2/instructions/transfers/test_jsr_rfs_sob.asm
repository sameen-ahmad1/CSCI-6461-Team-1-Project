LOC     10
        LDA     1,0,3        ;R1 <- 3
LOOP:   SOB     1,0,LOOP     ;R1--; branch while R1>0
        JNE     1,0,SOB_FAIL ;R1 must be 0
        AIR     3,1          ;PASS GPR[3]=1
        JSR     0,SUBROUTINE ;GPR[3]<-return addr; PC<-SUBROUTINE
AFTER_JSR: LDA  3,0,2        ;GPR[3]=2
        HLT

SOB_FAIL: HLT
SUBROUTINE: RFS 4             ;R0<-4; PC<-c(GPR[3])