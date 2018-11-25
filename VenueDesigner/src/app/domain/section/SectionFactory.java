package app.domain.section;

import app.domain.Mode;
import app.domain.VitalSpace;
import app.domain.shape.Shape;

public final class SectionFactory {
    public static Section create(Mode mode, Shape shape) {
        switch (mode) {
            case RegularSeatedSection:
                return new SeatedSection("", 0, shape, new VitalSpace(20, 20));
            case IrregularSeatedSection:
                return new SeatedSection("", 0, shape, new VitalSpace(20, 20));
            case RegularStandingSection:
                return new StandingSection("",0, shape,10);
            case IrregularStandingSection:
                return new StandingSection("",0, shape,10);
        }
        throw new RuntimeException();
    }
}
