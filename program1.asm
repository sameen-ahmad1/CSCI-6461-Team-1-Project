LOC     0
        Data    RSign
        Data    TgRead
        Data    TAcc
        Data    TSign
        Data    AbsOK
        Data    TDoAcc


        LOC     6
        Data    0           ; [6]  counter
        Data    0           ; [7]  sign flag
        Data    0           ; [8]  digit accumulator
        Data    0           ; [9]  target number
        Data    0           ; [10] best diff
        Data    0           ; [11] closest index
        Data    0           ; [12] scratch
        Data    10          ; [13] constant: 10
        Data    48          ; [14] constant: ASCII '0'
        Data    13          ; [15] constant: ASCII CR
        Data    45          ; [16] constant: ASCII '-'
        Data    20          ; [17] constant: N = 20
        Data    32          ; [18] constant: array base address


        Data    RdLp        ; [19]
        Data    PrLp        ; [20]
        Data    PrPrompt    ; [21]
        Data    SrLp        ; [22]
        Data    Done        ; [23]
        Data    Stor        ; [24]
        Data    Sv          ; [25]
        Data    Neg         ; [26]
        Data    Dgt         ; [27]
        Data    Acc         ; [28]
        Data    TNeg        ; [29]
        Data    TSv         ; [30]
        Data    Update      ; [31]


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


        LOC     60
        LDA     0,0,0       ; R0 = 0
        STR     0,0,6       ; counter  = 0
        STR     0,0,7       ; sign     = 0
        STR     0,0,8       ; acc      = 0
        LDR     0,0,14      ; R0 = 48
        AIR     0,21        ; R0 = 69 ('E')
        OUT     0,1
        AIR     0,31        ; R0 = 100
        AIR     0,10        ; R0 = 110 ('n')
        OUT     0,1
        AIR     0,6         ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        AIR     0,13        ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,18      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,2         ; R0 = 50 ('2')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48 ('0')
        OUT     0,1
        LDR     0,0,18      ; R0 = 32 (' ')
        AIR     0,31        ; R0 = 63
        AIR     0,31        ; R0 = 94
        AIR     0,16        ; R0 = 110 ('n')
        OUT     0,1
        AIR     0,7         ; R0 = 117 ('u')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,30        ; R0 = 109 ('m')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,19        ; R0 = 98 ('b')
        OUT     0,1
        AIR     0,3         ; R0 = 101 ('e')
        OUT     0,1
        AIR     0,13        ; R0 = 114 ('r')
        OUT     0,1
        AIR     0,1         ; R0 = 115 ('s')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,10        ; R0 = 58 (:)
        OUT     0,1
        LDR     0,0,15      ; R0 = 13 ('\r')
        OUT     0,1
        LDR     0,0,13      ; R0 = 10 ('\n')
        OUT     0,1


RdLp:
        LDR     0,0,6       ; R0 = counter
        LDR     1,0,17      ; R1 = 20
        TRR     0,1         ; CC = equal if counter == 20
        LDX     1,20        ; IX1 -> PrLp
        JCC     3,1,0       ; counter == 20  =>  go print
        IN      0,0         ; read one character into R0
        LDR     1,0,15      ; R1 = CR (13)
        TRR     0,1
        LDX     1,24        ; IX1 -> Stor
        JCC     3,1,0       ; CR  =>  store current number
        LDR     1,0,13      ; R1 = LF (10)
        TRR     0,1
        LDX     1,24        ; IX1 -> Stor
        JCC     3,1,0       ; LF  =>  store current number
        LDR     1,0,16      ; R1 = '-' (45)
        TRR     0,1
        LDX     1,0         ; IX1 -> RSign  (via mem[0])
        JCC     3,1,0       ; '-'  =>  set sign flag
        LDX     1,27        ; IX1 -> Dgt
        JMA     1,0         ; else try as digit


RSign:
        LDA     1,0,1       ; R1 = 1
        STR     1,0,7       ; sign = 1
        LDX     1,19        ; IX1 -> RdLp
        JMA     1,0


Dgt:
        SMR     0,0,14      ; R0 = char - 48
        LDX     1,28        ; IX1 -> Acc
        JGE     0,1,0       ; R0 >= 0  =>  valid digit, accumulate
        LDX     1,19        ; IX1 -> RdLp
        JMA     1,0         ; else ignore character


Acc:
        STR     0,0,12      ; mem[12] = digit
        LDR     0,0,8       ; R0 = accumulator
        LDR     2,0,13      ; R2 = 10
        LDA     1,0,0       ; R1 = 0  (will receive low word from MLT)
        MLT     0,2         ; R0,R1 = acc * 10
        AMR     1,0,12      ; R1 += digit
        STR     1,0,8       ; acc = R1
        LDX     1,19        ; IX1 -> RdLp
        JMA     1,0


Stor:
        LDR     0,0,8       ; R0 = unsigned accumulated value
        LDR     1,0,7       ; R1 = sign flag
        LDA     2,0,1       ; R2 = 1
        TRR     1,2         ; sign == 1?
        LDX     1,26        ; IX1 -> Neg
        JCC     3,1,0       ; yes  =>  negate before storing
        LDX     1,25        ; IX1 -> Sv
        JMA     1,0         ; no   =>  store as-is


Neg:
        NOT     0
        AIR     0,1         ; R0 = -R0


Sv:
        LDR     2,0,18      ; R2 = 32  (base address)
        AMR     2,0,6       ; R2 = 32 + counter
        STR     2,0,12      ; mem[12] = target address
        LDX     1,12        ; IX1 = target address
        STR     0,1,0       ; array[counter] = R0
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; counter++
        LDA     0,0,0
        STR     0,0,7       ; sign = 0
        STR     0,0,8       ; acc  = 0
        LDX     1,19        ; IX1 -> RdLp
        JMA     1,0


PrLp:
        LDA     1,0,0       ; R1 = 0
        LDR     0,0,6       ; R0 = counter
        TRR     0,1         ; CC = equal if counter == 0
        LDX     1,21        ; IX1 -> TgLp
        JCC     3,1,0       ; counter == 0  =>  done printing
        NOT     0           ;  \
        AIR     0,1         ;   >  R0 = counter - 1
        NOT     0           ;  /
        STR     0,0,6       ; counter--
        LDR     2,0,18      ; R2 = 32
        AMR     2,0,6       ; R2 = 32 + (counter after decrement)
        STR     2,0,12      ; mem[12] = array address
        LDX     1,12        ; IX1 = array address
        LDA     0,0,1       ; sentinel: next OUT is an integer
        OUT     0,1
        LDR     0,1,0       ; R0 = array[counter]
        OUT     0,1         ; print to console printer (device 1)
        LDR     0,0,15      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,13      ; R0 = 10 (LF)
        OUT     0,1
        LDX     1,20        ; IX1 -> PrLp
        JMA     1,0         ; loop (counter already decremented)


PrPrompt:
        LDR     0,0,14      ; R0 = 48
        AIR     0,21        ; R0 = 69 ('E')
        OUT     0,1
        AIR     0,31        ; R0 = 100
        AIR     0,10        ; R0 = 110 ('n')
        OUT     0,1
        AIR     0,6         ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        AIR     0,13        ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,18      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,18        ; R0 = 97 ('a')
        OUT     0,1
        LDR     0,0,18      ; R0 = 32 (' ')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,6         ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,18        ; R0 = 97 ('a')
        OUT     0,1
        AIR     0,17        ; R0 = 114 ('r')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,24        ; R0 = 103 ('g')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 ('e')
        OUT     0,1
        AIR     0,15        ; R0 = 116 ('t')
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,10        ; R0 = 58 (:)
        OUT     0,1
        LDR     0,0,15      ; R0 = 13 ('\r')
        OUT     0,1
        LDR     0,0,13      ; R0 = 10 ('\n')
        OUT     0,1

TgLp:
        LDA     0,0,0
        STR     0,0,7       ; sign = 0
        STR     0,0,8       ; acc  = 0


TgRead:
        IN      0,0
        LDR     1,0,15      ; R1 = CR
        TRR     0,1
        LDX     1,30        ; IX1 -> TSv
        JCC     3,1,0       ; CR  =>  save target
        LDR     1,0,13      ; R1 = LF (10)
        TRR     0,1
        LDX     1,30        ; IX1 -> TSv
        JCC     3,1,0       ; LF  =>  save target
        LDR     1,0,16      ; R1 = '-'
        TRR     0,1
        LDX     1,3         ; IX1 -> TSign  (via mem[3])
        JCC     3,1,0       ; '-' =>  set sign
        LDX     1,2         ; IX1 -> TAcc   (via mem[2])
        JMA     1,0


TSign:
        LDA     1,0,1
        STR     1,0,7       ; sign = 1
        LDX     1,1         ; IX1 -> TgRead  (via mem[1])
        JMA     1,0


TAcc:
        SMR     0,0,14      ; R0 = char - 48
        LDX     1,5         ; IX1 -> TDoAcc  (via mem[5])
        JGE     0,1,0       ; valid digit  =>  accumulate
        LDX     1,1         ; IX1 -> TgRead
        JMA     1,0


TDoAcc:
        STR     0,0,12
        LDR     0,0,8
        LDR     2,0,13      ; R2 = 10
        LDA     1,0,0
        MLT     0,2
        AMR     1,0,12
        STR     1,0,8
        LDX     1,1         ; IX1 -> TgRead
        JMA     1,0


TSv:
        LDR     0,0,8       ; R0 = accumulated value (negated if TNeg ran)
        LDR     1,0,7       ; R1 = sign flag (0 after TNeg fix)
        LDA     2,0,1       ; R2 = 1
        TRR     1,2         ; sign == 1?
        LDX     1,29        ; IX1 -> TNeg
        JCC     3,1,0       ; yes  =>  negate
        STR     0,0,9       ; target = R0 (correct signed value)
        LDA     0,0,0
        NOT     0           ; R0 = 0xFFFF
        SRC     0,1,0,1     ; logical right shift by 1 -> R0 = 0x7FFF = 32767
        STR     0,0,10      ; best_diff = 32767  (max possible, any real diff beats it)
        LDA     0,0,0
        STR     0,0,11      ; closest_index = 0  (default)
        STR     0,0,6       ; counter = 0  (restart from first element)
        LDX     1,22        ; IX1 -> SrLp
        JMA     1,0


TNeg:
        NOT     0
        AIR     0,1         ; R0 = -R0  (two's complement)
        STR     0,0,8       ; mem[8] = negated value  (TSv will reload this)
        LDA     0,0,0
        STR     0,0,7       ; sign = 0  (so TSv's branch-to-TNeg does NOT fire)
        LDX     1,30        ; IX1 -> TSv
        JMA     1,0


SrLp:
        LDR     0,0,6       ; R0 = counter
        LDR     1,0,17      ; R1 = 20
        TRR     0,1
        LDX     1,23        ; IX1 -> Done
        JCC     3,1,0       ; counter == 20  =>  search complete
        LDR     2,0,18      ; R2 = 32
        AMR     2,0,6       ; R2 = 32 + counter
        STR     2,0,12      ; mem[12] = array element address
        LDX     1,12        ; IX1 = array element address
        LDR     1,1,0       ; R1 = array[counter]
        SMR     1,0,9       ; R1 = array[counter] - target
        LDX     1,4         ; IX1 -> AbsOK  (via mem[4])
        JGE     1,1,0       ; diff >= 0  =>  already positive
        NOT     1
        AIR     1,1         ; R1 = |diff|


AbsOK:
        STR     1,0,12      ; mem[12] = |diff|
        LDR     0,0,10      ; R0 = best_diff
        SMR     0,0,12      ; R0 = best_diff - |diff|
        LDX     1,31        ; IX1 -> Update
        JGE     0,1,0       ; best_diff >= |diff|  =>  new best, update
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6       ; counter++
        LDX     1,22        ; IX1 -> SrLp
        JMA     1,0


Update:
        LDR     0,0,12      ; R0 = |diff|
        STR     0,0,10      ; best_diff = |diff|
        LDR     0,0,6       ; R0 = counter
        STR     0,0,11      ; closest_index = counter
        AIR     0,1
        STR     0,0,6       ; counter++  (FIX 3)
        LDX     1,22        ; IX1 -> SrLp
        JMA     1,0


Done:
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,5         ; R0 = 84 (T)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,18        ; R0 = 97 (a)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101
        AIR     0,13        ; R0 = 114 (r)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,24        ; R0 = 103 (g)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 (e)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,6         ; R0 = 116 (t)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,10        ; R0 = 58 (:)
        OUT     0,1
        LDR     0,0,18      ; R0 = 32 ( )
        OUT     0,1
        LDA     0,0,1       ; sentinel: next OUT is an integer
        OUT     0,1
        LDR     0,0,9       ; R0 = target number
        OUT     0,1         ; print target
        LDR     0,0,15      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,13      ; R0 = 10 (LF)
        OUT     0,1
        LDR     2,0,18      ; R2 = 32
        AMR     2,0,11      ; R2 = 32 + closest_index
        STR     2,0,12      ; mem[12] = address of closest element
        LDX     1,12        ; IX1 = that address
        LDR     0,0,14      ; R0 = 48
        AIR     0,19        ; R0 = 67 (C)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,29        ; R0 = 108 (l)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,1         ; R0 = 111 (o)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,5         ; R0 = 115 (s)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,22        ; R0 = 101 (e)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,5         ; R0 = 115 (s)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,31        ; R0 = 79
        AIR     0,31        ; R0 = 110
        AIR     0,6         ; R0 = 116 (t)
        OUT     0,1
        LDR     0,0,14      ; R0 = 48
        AIR     0,10        ; R0 = 58 (:)
        OUT     0,1
        LDR     0,0,18      ; R0 = 32 ( )
        OUT     0,1
        LDA     0,0,1       ; sentinel: next OUT is an integer
        OUT     0,1
        LDR     0,1,0       ; R0 = array[closest_index]
        OUT     0,1         ; print closest number
        LDR     0,0,15      ; R0 = 13 (CR)
        OUT     0,1
        LDR     0,0,13      ; R0 = 10 (LF)
        OUT     0,1
        HLT