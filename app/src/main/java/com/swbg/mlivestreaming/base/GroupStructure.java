package com.swbg.mlivestreaming.base;


class GroupStructure {
    private boolean hasHeader;
    private boolean hasFooter;
    private int childrenCount;

    GroupStructure(boolean hasHeader, boolean hasFooter, int childrenCount) {
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
        this.childrenCount = childrenCount;
    }

    boolean hasHeader() {
        return this.hasHeader;
    }

    void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    boolean hasFooter() {
        return this.hasFooter;
    }

    void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    int getChildrenCount() {
        return this.childrenCount;
    }

    void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }
}
