LOC 0
Data 0      ; 0 = input counter
Data 0      ; 1 = temp
Data 0      ; 2 = buffer base index
Data 0      ; 3 = length

; -----------------------
; INPUT LOOP
; -----------------------

Start:
        CHK 0,2
        JZ 0,0,FinishInput

        IN 0,2

        STR 0,0,2

        LDR 1,0,0
        AIR 1,1
        STR 1,0,0

        LDR 2,0,2
        AIR 2,1
        STR 2,0,2

        JMA 0,Start


FinishInput:
        LDR 1,0,0
        STR 1,0,3


; reset index for printing
        LDA 2,0,0


; -----------------------
; OUTPUT LOOP
; -----------------------

PrintLoop:
        LDR 1,0,2
        LDR 3,0,3

        TRR 2,3
        JCC 3,0,Done


        LDR 0,0,2
        OUT 0,1

        LDR 2,0,2
        AIR 2,1

        JMA 0,PrintLoop


Done:
        HLT