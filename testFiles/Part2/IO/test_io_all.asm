LOC 0
        IN   0, 0       ; GPR[0] <- keyboard
        IN   1, 0       ; GPR[1] <- keyboard
        IN   2, 2       ; GPR[2] <- card reader
        IN   3, 2       ; GPR[3] <- card reader
        OUT  0, 1       ; printer <- GPR[0]
        OUT  1, 1       ; printer <- GPR[1]
        OUT  2, 1       ; printer <- GPR[2]
        OUT  3, 1       ; printer <- GPR[3]
        HLT