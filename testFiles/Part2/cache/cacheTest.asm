; cache test - hits and misses
; GPR[3] = 2 means both tests passed

        LOC 0
ZERO:   DATA 0
TVAL:   DATA 42

        LOC 8
        LDR 0, 0, 1         ; miss - GPR[0] = 42
        LDR 2, 0, 1         ; hit  - GPR[2] = 42
        TRR 0, 2
        JCC 3, 0, T1OK
        HLT
T1OK:   AIR 3, 1            ; GPR[3] = 1

        LDA 2, 0, 5         ; loop counter
LOOP:   LDR 0, 0, 1         ; hits
        SOB 2, 0, LOOP
        AIR 3, 1            ; GPR[3] = 2

        HLT