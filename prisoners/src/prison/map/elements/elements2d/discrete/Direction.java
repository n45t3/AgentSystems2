package prison.map.elements.elements2d.discrete;

public enum Direction {
    N,
    // NE,
    E,
    // SE,
    S,
    // SW,
    W,
    // NW,
    UNDEF;

    public static Direction get(int dx, int dy) {
        if (dx == 1) return E;
        if (dx == -1) return W;
        if (dy == 1) return S;
        if (dy == -1) return N;
        return UNDEF;
    }

    public int[] move(int x, int y) {
        int[] out = new int[] { x, y };
        switch (this) {
        // case NW:
        // out[0]--;
        // out[1]--;
        // break;
        case N:
            out[1]--;
            break;
        // case NE:
        // out[0]++;
        // out[1]--;
        // break;
        case E:
            out[0]++;
            break;
        // case SE:
        // out[0]++;
        // out[1]++;
        // break;
        case S:
            out[1]++;
            break;
        // case SW:
        // out[0]--;
        // out[1]++;
        // break;
        case W:
            out[0]--;
            break;
        case UNDEF:
            break;
        }
        return out;
    }
}

/*
 * -1,-1 0,-1 +1,-1 -1, 0 0, 0 +1, 0 -1,+1 0,+1 +1,+1
 */