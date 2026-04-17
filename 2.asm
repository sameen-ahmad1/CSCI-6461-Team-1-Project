LOC     0
        Data    6
        Data    6

        LOC     6
        Data    0           ; [6]  idx (general purpose index)
        Data    0           ; [7]  bufLen (total chars stored in flat buffer)
        Data    0           ; [8]  wordCount
        Data    0           ; [9]  foundFlag
        Data    0           ; [10] foundSent
        Data    0           ; [11] foundWord
        Data    0           ; [12] scratch (indirect address)
        Data    0           ; [13] wbufIdx / scratch2
        Data    0           ; [14] sentCount (periods seen so far in search)
        Data    50          ; [15] const_50
        Data    48          ; [16] const_48
        Data    32          ; [17] const_32 (space)
        Data    10          ; [18] const_10 (LF)
        Data    13          ; [19] const_13 (CR)
        Data    WordPrompt  ; [20] WordPrompt address
        Data    46          ; [21] const_46 '.'
        Data    33          ; [22] const_33 '!'
        Data    63          ; [23] const_63 '?'
        Data    500         ; [24] const_wbuf (word buffer base)
        Data    400         ; [25] const_buf  (flat text buffer base)
        Data    RdLp        ; [26]
        Data    SrLoop      ; [27] search loop target
        Data    PrDone      ; [28] used by RdChar for CR/LF
        Data    RdWord      ; [29]
        Data    ENDWORD     ; [30]
        Data    SrNoMatch   ; [31]

        LOC     400
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0

        LOC     500
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0

LOC     600

Init:
        LDA     0,0,0
        STR     0,0,6       ; idx = 0
        STR     0,0,7       ; bufLen = 0
        ; fall through into RdLp

RdLp:
RdChar:
        CHK     0,2
        LDA     1,0,0
        TRR     0,1
        LDX     1,28
        JCC     3,1,0       ; no input ready → PrDone

        IN      0,2

        ; skip null
        LDA     1,0,0
        TRR     0,1
        LDX     1,26
        JCC     3,1,0       ; null → loop back

        ; CR → end of line
        LDR     1,0,19
        TRR     0,1
        LDX     1,28
        JCC     3,1,0       ; → PrDone

        ; LF → end of line
        LDR     1,0,18
        TRR     0,1
        LDX     1,28
        JCC     3,1,0       ; → PrDone

        ; store char to flat buffer
        STR     0,0,13
        LDR     2,0,25
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDR     0,0,13
        STR     0,1,0

        ; print char immediately
        OUT     0,1

        ; increment idx and loop back
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,26
        JMA     1,0

PrDone:
        LDR     0,0,6
        STR     0,0,7       ; bufLen = idx
        LDA     0,0,0
        STR     0,0,6       ; idx = 0
        LDX     1,20
        JMA     1,0         ; → WordPrompt

WordPrompt:
        LDR     0,0,19
        OUT     0,1
        LDR     0,0,18
        OUT     0,1
        LDR     0,0,16
        AIR     0,21
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        AIR     0,6
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,22
        OUT     0,1
        AIR     0,13
        OUT     0,1
        LDR     0,0,17
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        AIR     0,9
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        AIR     0,1
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        AIR     0,4
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,21
        OUT     0,1
        LDR     0,0,16
        AIR     0,10
        OUT     0,1
        LDR     0,0,19
        OUT     0,1
        LDR     0,0,18
        OUT     0,1

        LDA     0,0,0
        STR     0,0,6       ; idx = 0
        LDX     1,29
        JMA     1,0         ; → RdWord

RdWord:
        IN      0,0

        ; CR
        LDR     1,0,19
        TRR     0,1
        LDX     1,30
        JCC     3,1,0       ; → ENDWORD

        ; LF
        LDR     1,0,18
        TRR     0,1
        LDX     1,30
        JCC     3,1,0       ; → ENDWORD

        ; echo char
        OUT     0,1

        ; store char to word buffer
        STR     0,0,13
        LDR     2,0,24
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDR     0,0,13
        STR     0,1,0

        ; idx++
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,29
        JMA     1,0         ; → RdWord

ENDWORD:
        ; null terminate word buffer
        LDR     2,0,24
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDA     0,0,0
        STR     0,1,0

        ; init search state
        LDA     0,0,0
        STR     0,0,6       ; idx = 0
        STR     0,0,9       ; foundFlag = 0
        STR     0,0,14      ; sentCount = 0
        LDA     0,0,1
        STR     0,0,8       ; wordCount = 1

SrLoop:
        LDR     0,0,7       ; bufLen
        LDR     1,0,6       ; idx
        SMR     0,1,0       ; bufLen - idx
        LDX     1,20
        JCC     1,1,0       ; jump to WordPrompt if idx >= bufLen

        ; load current char from flat buffer
        LDR     2,0,25
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDR     0,1,0
        STR     0,0,13      ; save current char

        ; check '.'
        LDR     1,0,21
        TRR     0,1
        LDX     1,27
        JCC     3,1,SrEndSent

        ; check '!'
        LDR     1,0,22
        TRR     0,1
        LDX     1,27
        JCC     3,1,SrEndSent

        ; check '?'
        LDR     1,0,23
        TRR     0,1
        LDX     1,27
        JCC     3,1,SrEndSent

        ; check space
        LDR     1,0,17
        TRR     0,1
        LDX     1,27
        JCC     3,1,SrSpace

        ; try to match word, reset wbufIdx
        LDA     0,0,0
        STR     0,0,13

CmpLoop:
        ; load char from word buffer at wbufIdx
        LDR     2,0,24
        AMR     2,0,13
        STR     2,0,12
        LDX     1,12
        LDR     1,1,0

        ; if null → full word matched, check boundary
        LDA     2,0,0
        TRR     1,2
        LDX     1,27
        JCC     3,1,ChkBound

        ; load flat buffer char at idx+wbufIdx
        LDR     2,0,6
        AMR     2,0,13
        LDR     3,0,25
        AMR     3,2,0
        STR     3,0,12
        LDX     2,12
        LDR     0,2,0

        ; compare
        TRR     0,1
        LDX     1,31
        JCC     3,1,0       ; mismatch → SrNoMatch

        ; match, advance wbufIdx
        LDR     0,0,13
        AIR     0,1
        STR     0,0,13
        LDX     1,27
        JMA     1,0         ; → CmpLoop

ChkBound:
        ; check char after match is a boundary
        LDR     2,0,6
        AMR     2,0,13
        LDR     3,0,25
        AMR     3,2,0
        STR     3,0,12
        LDX     2,12
        LDR     0,2,0

        LDA     1,0,0
        TRR     0,1
        LDX     1,27
        JCC     3,1,Found

        LDR     1,0,17
        TRR     0,1
        LDX     1,27
        JCC     3,1,Found

        LDR     1,0,21
        TRR     0,1
        LDX     1,27
        JCC     3,1,Found

        LDR     1,0,22
        TRR     0,1
        LDX     1,27
        JCC     3,1,Found

        LDR     1,0,23
        TRR     0,1
        LDX     1,27
        JCC     3,1,Found

SrNoMatch:
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,27
        JMA     1,0         ; → SrLoop

SrSpace:
        LDR     0,0,8
        AIR     0,1
        STR     0,0,8
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,27
        JMA     1,0         ; → SrLoop

SrEndSent:
        LDR     0,0,14
        AIR     0,1
        STR     0,0,14
        LDA     0,0,1
        STR     0,0,8       ; reset wordCount to 1
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,27
        JMA     1,0         ; → SrLoop

Found:
        LDA     0,0,1
        STR     0,0,9       ; foundFlag = 1
        LDR     0,0,14
        AIR     0,1
        STR     0,0,10      ; foundSent = sentCount + 1
        LDR     0,0,8
        STR     0,0,11      ; foundWord = wordCount

PrintResult:
        LDR     0,0,9
        LDA     1,0,0
        TRR     0,1
        LDX     1,27          ; reuse any convenient register load
        JCC     3,1,0         ; if foundFlag == 0, fall to NotFound

        ; --- FOUND path ---
        ; print foundSent ([10]) and foundWord ([11]) here
        HLT

NotFound:
        LDR     0,0,16
        AIR     0,30
        OUT     0,1
        HLT