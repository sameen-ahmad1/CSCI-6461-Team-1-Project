LOC     0
        Data    6
        Data    6


        LOC     6
        Data    0           ; [6]  charIdx
        Data    0           ; [7]  sentCount
        Data    0           ; [8]  wordCount
        Data    0           ; [9]  foundFlag
        Data    0           ; [10] foundSent
        Data    0           ; [11] foundWord
        Data    0           ; [12] scratch
        Data    0           ; [13] scratch2 / wbufIdx
        Data    32          ; [14] curSentBase
        Data    50          ; [15] const_50
        Data    48          ; [16] const_48
        Data    32          ; [17] const_32
        Data    10          ; [18] const_10
        Data    13          ; [19] const_13
        Data    6           ; [20] const_6
        Data    46          ; [21] const_46  '.'
        Data    33          ; [22] const_33  '!'
        Data    63          ; [23] const_63  '?'
        Data    332         ; [24] const_wbuf
        Data    RdLp        ; [25]
        Data    EndSent     ; [26]
        Data    RdChar      ; [27]
        Data    PrSentChk   ; [28]
        Data    PrChar      ; [29]
        Data    SrSentChk   ; [30]
        Data    SkipSp      ; [31]


        LOC     32
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


        LOC     332
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


        LOC     400
        LDA     0,0,0
        STR     0,0,6       ; charIdx = 0
        STR     0,0,7       ; sentCount = 0
        LDR     0,0,17      ; R0 = 32  (sentence storage base)
        STR     0,0,14      ; curSentBase = 32
        LDR     0,0,16      ; R0 = 48
        AIR     0,28        ; R0 = 76  ('L')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,1         ; R0 = 111 ('o')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,18        ; R0 = 97  ('a')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,21        ; R0 = 100 ('d')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,23        ; R0 = 102 ('f')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,26        ; R0 = 105 ('i')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,29        ; R0 = 108 ('l')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,26        ; R0 = 105 ('i')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110 ('n')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,6         ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,1         ; R0 = 111 ('o')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,20        ; R0 = 99  ('c')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,18        ; R0 = 97  ('a')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,4         ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,21        ; R0 = 100 ('d')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,4         ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,18        ; R0 = 97  ('a')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,21        ; R0 = 100 ('d')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,4         ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        SIR     0,4         ; R0 = 44  (',')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,6         ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,25        ; R0 = 104 ('h')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110 ('n')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,2         ; R0 = 112 ('p')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,4         ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,5         ; R0 = 115 ('s')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,5         ; R0 = 115 ('s')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,21        ; R0 = 69  ('E')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110 ('n')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,6         ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,4         ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,10        ; R0 = 58  (':')
        OUT     0,1
        LDR     0,0,19      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,18      ; R0 = 10 (LF)
        OUT     0,1
        IN      0,0         ; wait for user to press Enter


RdLp:
        LDR     0,0,7       ; R0 = sentCount
        LDR     1,0,20      ; R1 = 6
        TRR     0,1
        LDX     1,28        ; IX1 -> PrSentChk
        JCC     3,1,0       ; sentCount == 6  =>  go to print phase


RdChar:
        IN      0,2         ; R0 = char from card reader
        LDR     1,0,19      ; R1 = 13 (CR)
        TRR     0,1
        LDX     1,27        ; IX1 -> RdChar
        JCC     3,1,0       ; CR  =>  skip
        LDR     1,0,18      ; R1 = 10 (LF)
        TRR     0,1
        LDX     1,27        ; IX1 -> RdChar
        JCC     3,1,0       ; LF  =>  skip
        STR     0,0,13      ; scratch2 = char  (save before R0 clobbered)
        LDR     2,0,14      ; R2 = curSentBase
        AMR     2,0,6       ; R2 = curSentBase + charIdx
        STR     2,0,12      ; scratch = store target address
        LDX     1,12        ; IX1 = target address
        LDR     0,0,13      ; R0 = char  (reload)
        STR     0,1,0       ; mem[curSentBase + charIdx] = char
        LDR     1,0,21      ; R1 = 46 ('.')
        TRR     0,1
        LDX     1,26        ; IX1 -> EndSent
        JCC     3,1,0       ; '.'  =>  sentence done
        LDR     1,0,22      ; R1 = 33 ('!')
        TRR     0,1
        LDX     1,26        ; IX1 -> EndSent
        JCC     3,1,0       ; '!'  =>  sentence done
        LDR     1,0,23      ; R1 = 63 ('?')
        TRR     0,1
        LDX     1,26        ; IX1 -> EndSent
        JCC     3,1,0       ; '?'  =>  sentence done
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; charIdx++
        LDX     1,27        ; IX1 -> RdChar
        JMA     1,0


EndSent:
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; charIdx++  (step past terminator)
        LDR     2,0,14      ; R2 = curSentBase
        AMR     2,0,6       ; R2 = curSentBase + charIdx
        STR     2,0,12      ; scratch = address after terminator
        LDX     1,12
        LDA     0,0,0
        STR     0,1,0       ; write null terminator
        LDR     0,0,14      ; R0 = curSentBase
        AMR     0,0,15      ; R0 = curSentBase + 50
        STR     0,0,14      ; curSentBase += 50
        LDA     0,0,0
        STR     0,0,6       ; charIdx = 0
        LDR     0,0,7
        AIR     0,1
        STR     0,0,7       ; sentCount++
        LDX     1,25        ; IX1 -> RdLp
        JMA     1,0


PrSentChk:
        LDA     0,0,0
        STR     0,0,7       ; sentCount = 0
        LDR     0,0,17      ; R0 = 32
        STR     0,0,14      ; curSentBase = 32
        STR     0,0,6       ; charIdx = 0


PrNextSent:
        LDR     0,0,7       ; R0 = sentCount
        LDR     1,0,20      ; R1 = 6
        TRR     0,1
        LDX     1,30        ; IX1 -> SrSentChk  (move to search phase)
        JCC     3,1,0       ; sentCount == 6  =>  done printing


PrChar:
        LDR     2,0,14      ; R2 = curSentBase
        AMR     2,0,6       ; R2 = curSentBase + charIdx
        STR     2,0,12      ; scratch = address
        LDX     1,12
        LDR     0,1,0       ; R0 = mem[curSentBase + charIdx]
        LDA     1,0,0
        TRR     0,1
        LDX     1,29        ; IX1 -> PrChar  (used as loop-back)
        JCC     3,1,0       ; null  =>  end of sentence
        OUT     0,1
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; charIdx++
        LDX     1,29        ; IX1 -> PrChar
        JMA     1,0


        LDR     0,0,19      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,18      ; R0 = 10 (LF)
        OUT     0,1
        LDR     0,0,14      ; R0 = curSentBase
        AMR     0,0,15      ; R0 += 50
        STR     0,0,14      ; curSentBase += 50
        LDA     0,0,0
        STR     0,0,6       ; charIdx = 0
        LDR     0,0,7
        AIR     0,1
        STR     0,0,7       ; sentCount++
        LDX     1,28        ; IX1 -> PrSentChk  (check at top re-initialises nothing)
        JMA     1,0


        LDR     0,0,16      ; R0 = 48
        AIR     0,21        ; R0 = 69  ('E')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110 ('n')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,6         ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        AIR     0,13        ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,9         ; R0 = 119 ('w')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,1         ; R0 = 111 ('o')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,4         ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,21        ; R0 = 100 ('d')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,10        ; R0 = 58  (':')
        OUT     0,1
        LDR     0,0,19      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,18      ; R0 = 10 (LF)
        OUT     0,1
        LDA     0,0,0
        STR     0,0,6       ; charIdx = 0


RdWord:
        IN      0,0         ; R0 = char from keyboard
        LDR     1,0,19      ; R1 = 13 (CR)
        TRR     0,1
        LDX     1,30        ; IX1 -> SrSentChk
        JCC     3,1,0       ; CR  =>  done, go search
        LDR     1,0,18      ; R1 = 10 (LF)
        TRR     0,1
        LDX     1,30        ; IX1 -> SrSentChk
        JCC     3,1,0       ; LF  =>  done, go search
        STR     0,0,13      ; scratch2 = char
        LDR     2,0,24      ; R2 = wbuf base (332)
        AMR     2,0,6       ; R2 = wbuf + charIdx
        STR     2,0,12      ; scratch = target address
        LDX     1,12
        LDR     0,0,13      ; R0 = char
        STR     0,1,0       ; wbuf[charIdx] = char
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; charIdx++
        LDX     1,31        ; IX1 -> SkipSp  (reused as RdWord loopback below)
        JMA     1,0


        LDR     2,0,24      ; R2 = wbuf base
        AMR     2,0,6       ; R2 = wbuf + charIdx
        STR     2,0,12
        LDX     1,12
        LDA     0,0,0
        STR     0,1,0       ; null terminator


SrSentChk:
        LDA     0,0,0
        STR     0,0,7       ; sentCount = 0
        STR     0,0,9       ; foundFlag = 0
        LDR     0,0,17      ; R0 = 32
        STR     0,0,14      ; curSentBase = 32


SrNextSent:
        LDR     0,0,7       ; R0 = sentCount
        LDR     1,0,20      ; R1 = 6
        TRR     0,1
        LDX     1,31        ; IX1 -> SkipSp  (repurposed as Done jump below)
        JCC     3,1,0       ; sentCount == 6  =>  all sentences searched, print result
        LDA     0,0,0
        STR     0,0,6       ; charIdx = 0
        LDA     0,0,1
        STR     0,0,8       ; wordCount = 1


SkipSp:
        LDR     2,0,14      ; R2 = curSentBase
        AMR     2,0,6       ; R2 = curSentBase + charIdx
        STR     2,0,12
        LDX     1,12
        LDR     0,1,0       ; R0 = char at current position
        LDA     1,0,0
        TRR     0,1
        LDX     1,30        ; IX1 -> SrNextSent  (end of sentence, try next)
        JCC     3,1,0       ; null  =>  end of sentence
        LDR     1,0,17      ; R1 = 32 (space)
        TRR     0,1
        LDX     1,31        ; IX1 -> SkipSp (not space: go compare)
        JCC     3,1,0       ; not a space  =>  start comparing word
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; charIdx++  (skip the space)
        LDX     1,31        ; IX1 -> SkipSp
        JMA     1,0


CmpStart:
        LDA     0,0,0
        STR     0,0,13      ; wbufIdx = 0


CmpLoop:
        LDR     2,0,14      ; R2 = curSentBase
        AMR     2,0,6       ; R2 = curSentBase + charIdx
        STR     2,0,12
        LDX     1,12
        LDR     0,1,0       ; R0 = sentence char
        LDR     2,0,24      ; R2 = wbuf base
        AMR     2,0,13      ; R2 = wbuf + wbufIdx
        STR     2,0,12
        LDX     1,12
        LDR     1,1,0       ; R1 = wbuf char
        LDA     2,0,0
        TRR     1,2
        LDX     1,30        ; IX1 -> SrNextSent  (repurposed; ChkBound is inline)
        JCC     3,1,0       ; wbuf char == 0  =>  check word boundary
        TRR     0,1
        LDX     1,30        ; IX1 -> SrNextSent  (repurposed; NoMatch inline)
        JCC     3,1,0       ; chars equal  =>  advance both pointers
        LDX     1,31        ; IX1 -> SkipSp  (repurposed as NoMatch jump)
        JMA     1,0         ; chars differ  =>  skip to next word


        LDR     0,0,6       ; (chars matched: advance charIdx and wbufIdx)
        AIR     0,1
        STR     0,0,6
        LDR     0,0,13
        AIR     0,1
        STR     0,0,13
        LDX     1,30        ; IX1 -> SrNextSent  (repurposed as CmpLoop jump)
        JMA     1,0


ChkBound:
        LDA     1,0,0
        TRR     0,1
        LDX     1,30        ; IX1 -> SrNextSent  (repurposed as Found jump)
        JCC     3,1,0       ; sentence char null  =>  match at end of sentence
        LDR     1,0,17      ; R1 = 32 (space)
        TRR     0,1
        LDX     1,30        ; IX1 -> SrNextSent  (repurposed as Found jump)
        JCC     3,1,0       ; space  =>  word boundary match
        LDR     1,0,21      ; R1 = 46 ('.')
        TRR     0,1
        LDX     1,30        ; IX1 -> SrNextSent  (repurposed as Found jump)
        JCC     3,1,0
        LDR     1,0,22      ; R1 = 33 ('!')
        TRR     0,1
        LDX     1,30        ; IX1 -> SrNextSent  (repurposed as Found jump)
        JCC     3,1,0
        LDR     1,0,23      ; R1 = 63 ('?')
        TRR     0,1
        LDX     1,30        ; IX1 -> SrNextSent  (repurposed as Found jump)
        JCC     3,1,0       ; sentence terminator  =>  match


NoMatch:
        LDR     2,0,14      ; R2 = curSentBase
        AMR     2,0,6       ; R2 = curSentBase + charIdx
        STR     2,0,12
        LDX     1,12
        LDR     0,1,0       ; R0 = current char
        LDA     1,0,0
        TRR     0,1
        LDX     1,30        ; IX1 -> SrNextSent
        JCC     3,1,0       ; null  =>  end of sentence
        LDR     1,0,17      ; R1 = space
        TRR     0,1
        LDX     1,30        ; IX1 -> SrNextSent  (IncWord is inline below)
        JCC     3,1,0       ; space  =>  end of this word
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; charIdx++
        LDX     1,30        ; IX1 -> SrNextSent  (repurposed as NoMatch loopback)
        JMA     1,0


IncWord:
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; charIdx++  (step past space)
        LDR     0,0,8
        AIR     0,1
        STR     0,0,8       ; wordCount++
        LDX     1,31        ; IX1 -> SkipSp
        JMA     1,0


NextSent:
        LDR     0,0,14      ; R0 = curSentBase
        AMR     0,0,15      ; R0 += 50
        STR     0,0,14      ; curSentBase += 50
        LDR     0,0,7
        AIR     0,1
        STR     0,0,7       ; sentCount++
        LDX     1,30        ; IX1 -> SrSentChk  (will reinit loop vars)
        JMA     1,0


Found:
        LDA     0,0,1
        STR     0,0,9       ; foundFlag = 1
        LDR     0,0,7
        AIR     0,1         ; 1-based sentence number
        STR     0,0,10      ; foundSent
        LDR     0,0,8
        STR     0,0,11      ; foundWord


PrintResult:
        LDR     0,0,9       ; R0 = foundFlag
        LDA     1,0,0
        TRR     0,1
        LDX     1,31        ; IX1 -> SkipSp  (repurposed as NotFound jump)
        JCC     3,1,0       ; foundFlag == 0  =>  not found


PrintFound:
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,8         ; R0 = 87  ('W')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,1         ; R0 = 111 ('o')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,4         ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,21        ; R0 = 100 ('d')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,10        ; R0 = 58  (':')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDA     0,0,0
        STR     0,0,6       ; charIdx = 0


PrWordLoop:
        LDR     2,0,24      ; R2 = wbuf base
        AMR     2,0,6       ; R2 = wbuf + charIdx
        STR     2,0,12
        LDX     1,12
        LDR     0,1,0       ; R0 = wbuf[charIdx]
        LDA     1,0,0
        TRR     0,1
        LDX     1,31        ; IX1 -> SkipSp  (repurposed as PrSentNum jump)
        JCC     3,1,0       ; null  =>  done printing word
        OUT     0,1
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; charIdx++
        LDX     1,31        ; IX1 -> SkipSp  (repurposed as PrWordLoop jump)
        JMA     1,0


PrSentNum:
        LDR     0,0,19      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,18      ; R0 = 10 (LF)
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,4         ; R0 = 83  ('S')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110 ('n')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,6         ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110 ('n')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,20        ; R0 = 99  ('c')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,10        ; R0 = 58  (':')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,10      ; R0 = foundSent (1-6)
        AMR     0,0,16      ; R0 += 48  =>  ASCII digit
        OUT     0,1
        LDR     0,0,19      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,18      ; R0 = 10 (LF)
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,8         ; R0 = 87  ('W')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,1         ; R0 = 111 ('o')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,4         ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,21        ; R0 = 100 ('d')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,10        ; R0 = 58  (':')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,11      ; R0 = foundWord
        AMR     0,0,16      ; R0 += 48  =>  ASCII digit
        OUT     0,1
        LDR     0,0,19      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,18      ; R0 = 10 (LF)
        OUT     0,1
        HLT


NotFound:
        LDR     0,0,16      ; R0 = 48
        AIR     0,30        ; R0 = 78  ('N')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,1         ; R0 = 111 ('o')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,6         ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,17      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,23        ; R0 = 102 ('f')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,7         ; R0 = 117 ('u')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110 ('n')
        OUT     0,1
        LDR     0,0,16      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,21        ; R0 = 100 ('d')
        OUT     0,1
        LDR     0,0,19      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,18      ; R0 = 10 (LF)
        OUT     0,1
        HLT