package app.domain.shape;

public enum Mode {
    Rectangle(0), Polygon(1);

    private final int m_mode;

    Mode(int p_mode) {
        this.m_mode = p_mode;
    }

    public AbstractShape build() {
        switch (this.m_mode) {
            case 0:
                return new Rectangle();
            case 1:
                return new Polygon();
        }
        throw new RuntimeException();
    }
}
