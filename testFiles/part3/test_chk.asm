        LOC     010

Loop:
        CHK     0,2             ; GPR[0] = card reader status: 1=ready, 0=empty
        JZ      0,0,Done        ; if GPR[0] == 0 (empty), stop
        IN      0,2             ; GPR[0] = next card word (ASCII char code)
        OUT     0,1             ; send GPR[0] to printer (devid 1)
        JMA     0,Loop          ; go back and check again

Done:
        HLT
