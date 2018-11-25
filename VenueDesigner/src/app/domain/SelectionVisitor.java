package app.domain;

import app.domain.section.SeatedSection;

public interface SelectionVisitor {
    void visit(Stage stage);
    void visit(SeatedSection section);
}
