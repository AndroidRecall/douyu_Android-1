package com.swbg.mlivestreaming.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.iyao.recyclerviewhelper.adapter.CacheViewHolder;

import java.util.ArrayList;

public abstract class GroupedRecyclerViewAdapter extends RecyclerView.Adapter<CacheViewHolder> {
    public static final int TYPE_HEADER = 0x7f0c0009;
    public static final int TYPE_FOOTER = 0x7f0c000a;
    public static final int TYPE_CHILD = 0x7f0c000b;
    protected Context mContext;
    private ArrayList<GroupStructure> mStructures = new ArrayList<>();
    private boolean isDataChanged;
    private int mTempPosition;

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.structureChanged();
    }

    public GroupedRecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.registerAdapterDataObserver(new GroupedRecyclerViewAdapter.GroupDataObserver());
    }

    public CacheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(this.getLayoutId(this
                .mTempPosition, viewType), parent, false);
        CacheViewHolder viewHolder = new CacheViewHolder(view);
        onViewHolderCreated(viewHolder, viewType);
        return viewHolder;
    }


    public void onBindViewHolder(final CacheViewHolder holder, int position) {
        int type = this.judgeType(position);
        final int groupPosition = this.getGroupPositionForPosition(position);
        if (type == TYPE_HEADER) {
            this.onBindHeaderViewHolder(holder, groupPosition);
        } else if (type == TYPE_FOOTER) {
            this.onBindFooterViewHolder(holder, groupPosition);
        } else if (type == TYPE_CHILD) {
            final int childPosition = this.getChildPositionForPosition(groupPosition, position);
            this.onBindChildViewHolder(holder, groupPosition, childPosition);
        }

    }

    public int getItemCount() {
        if (this.isDataChanged) {
            this.structureChanged();
        }

        return this.count();
    }

    public int getItemViewType(int position) {
        this.mTempPosition = position;
        int groupPosition = this.getGroupPositionForPosition(position);
        int type = this.judgeType(position);
        if (type == TYPE_HEADER) {
            return this.getHeaderViewType(groupPosition);
        } else if (type == TYPE_FOOTER) {
            return this.getFooterViewType(groupPosition);
        } else if (type == TYPE_CHILD) {
            int childPosition = this.getChildPositionForPosition(groupPosition, position);
            return this.getChildViewType(groupPosition, childPosition);
        } else {
            return super.getItemViewType(position);
        }
    }

    public int getHeaderViewType(int groupPosition) {
        return TYPE_HEADER;
    }

    public int getFooterViewType(int groupPosition) {
        return TYPE_FOOTER;
    }

    public int getChildViewType(int groupPosition, int childPosition) {
        return TYPE_CHILD;
    }

    private int getLayoutId(int position, int viewType) {
        int type = this.judgeType(position);
        return type == TYPE_HEADER ? this.getHeaderLayout(viewType) : (type == TYPE_FOOTER ? this
                .getFooterLayout(viewType) : (type == TYPE_CHILD ? this.getChildLayout(viewType)
                : 0));
    }

    private int count() {
        return this.countGroupRangeItem(0, this.mStructures.size());
    }

    public int judgeType(int position) {
        int itemCount = 0;
        int groupCount = this.mStructures.size();

        for (int i = 0; i < groupCount; ++i) {
            GroupStructure structure = this.mStructures.get(i);
            if (structure.hasHeader()) {
                ++itemCount;
                if (position < itemCount) {
                    return TYPE_HEADER;
                }
            }

            itemCount += structure.getChildrenCount();
            if (position < itemCount) {
                return TYPE_CHILD;
            }

            if (structure.hasFooter()) {
                ++itemCount;
                if (position < itemCount) {
                    return TYPE_FOOTER;
                }
            }
        }

        return 0;
    }

    private void structureChanged() {
        this.mStructures.clear();
        int groupCount = this.getGroupCount();

        for (int i = 0; i < groupCount; ++i) {
            this.mStructures.add(new GroupStructure(this.hasHeader(i), this.hasFooter(i), this
                    .getChildrenCount(i)));
        }

        this.isDataChanged = false;
    }

    public int getGroupPositionForPosition(int position) {
        int count = 0;
        int groupCount = this.mStructures.size();

        for (int i = 0; i < groupCount; ++i) {
            count += this.countGroupItem(i);
            if (position < count) {
                return i;
            }
        }

        return -1;
    }

    public int getChildPositionForPosition(int groupPosition, int position) {
        if (groupPosition >= 0 && groupPosition < this.mStructures.size()) {
            int itemCount = this.countGroupRangeItem(0, groupPosition + 1);
            GroupStructure structure = this.mStructures.get(groupPosition);
            int p = structure.getChildrenCount() - (itemCount - position) + (structure.hasFooter
                    () ? 1 : 0);
            if (p >= 0) {
                return p;
            }
        }

        return -1;
    }

    public int getPositionForGroupHeader(int groupPosition) {
        if (groupPosition < this.mStructures.size()) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            return !structure.hasHeader() ? -1 : this.countGroupRangeItem(0, groupPosition);
        } else {
            return -1;
        }
    }

    public int getPositionForGroupFooter(int groupPosition) {
        if (groupPosition < this.mStructures.size()) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            return !structure.hasFooter() ? -1 : this.countGroupRangeItem(0, groupPosition + 1) - 1;
        } else {
            return -1;
        }
    }

    public int getPositionForChild(int groupPosition, int childPosition) {
        if (groupPosition < this.mStructures.size()) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            if (structure.getChildrenCount() > childPosition) {
                int itemCount = this.countGroupRangeItem(0, groupPosition);
                return itemCount + childPosition + (structure.hasHeader() ? 1 : 0);
            }
        }

        return -1;
    }

    public int countGroupItem(int groupPosition) {
        int itemCount = 0;
        if (groupPosition < this.mStructures.size()) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                ++itemCount;
            }

            itemCount += structure.getChildrenCount();
            if (structure.hasFooter()) {
                ++itemCount;
            }
        }

        return itemCount;
    }

    public int countGroupRangeItem(int start, int count) {
        int itemCount = 0;
        int size = this.mStructures.size();

        for (int i = start; i < size && i < start + count; ++i) {
            itemCount += this.countGroupItem(i);
        }

        return itemCount;
    }

    public void changeDataSet() {
        this.isDataChanged = true;
        this.notifyDataSetChanged();
    }

    public void changeGroup(int groupPosition) {
        int index = this.getPositionForGroupHeader(groupPosition);
        int itemCount = this.countGroupItem(groupPosition);
        if (index >= 0 && itemCount > 0) {
            this.notifyItemRangeChanged(index, itemCount);
        }

    }

    public void changeRangeGroup(int groupPosition, int count) {
        int index = this.getPositionForGroupHeader(groupPosition);
        boolean itemCount = false;
        int itemCount1;
        if (groupPosition + count <= this.mStructures.size()) {
            itemCount1 = this.countGroupRangeItem(groupPosition, groupPosition + count);
        } else {
            itemCount1 = this.countGroupRangeItem(groupPosition, this.mStructures.size());
        }

        if (index >= 0 && itemCount1 > 0) {
            this.notifyItemRangeChanged(index, itemCount1);
        }

    }

    public void changeHeader(int groupPosition) {
        int index = this.getPositionForGroupHeader(groupPosition);
        if (index >= 0) {
            this.notifyItemChanged(index);
        }
    }

    public void changeFooter(int groupPosition) {
        int index = this.getPositionForGroupFooter(groupPosition);
        if (index >= 0) {
            this.notifyItemChanged(index);
        }

    }

    public void changeChild(int groupPosition, int childPosition) {
        int index = this.getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            this.notifyItemChanged(index);
        }

    }

    public void changeRangeChild(int groupPosition, int childPosition, int count) {
        if (groupPosition < this.mStructures.size()) {
            int index = this.getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                GroupStructure structure = this.mStructures.get(groupPosition);
                if (structure.getChildrenCount() >= childPosition + count) {
                    this.notifyItemRangeChanged(index, count);
                } else {
                    this.notifyItemRangeChanged(index, structure.getChildrenCount() -
                            childPosition);
                }
            }
        }

    }

    public void changeChildren(int groupPosition) {
        if (groupPosition < this.mStructures.size()) {
            int index = this.getPositionForChild(groupPosition, 0);
            if (index >= 0) {
                GroupStructure structure = this.mStructures.get(groupPosition);
                this.notifyItemRangeChanged(index, structure.getChildrenCount());
            }
        }

    }

    public void removeAll() {
        this.notifyItemRangeRemoved(0, this.getItemCount());
        this.mStructures.clear();
    }

    public void removeGroup(int groupPosition) {
        int index = this.getPositionForGroupHeader(groupPosition);
        int itemCount = this.countGroupItem(groupPosition);
        if (index >= 0 && itemCount > 0) {
            this.notifyItemRangeRemoved(index, itemCount);
            this.notifyItemRangeChanged(index, this.getItemCount() - itemCount);
            this.mStructures.remove(groupPosition);
        }

    }

    public void removeRangeGroup(int groupPosition, int count) {
        int index = this.getPositionForGroupHeader(groupPosition);
        boolean itemCount = false;
        int itemCount1;
        if (groupPosition + count <= this.mStructures.size()) {
            itemCount1 = this.countGroupRangeItem(groupPosition, groupPosition + count);
        } else {
            itemCount1 = this.countGroupRangeItem(groupPosition, this.mStructures.size());
        }

        if (index >= 0 && itemCount1 > 0) {
            this.notifyItemRangeRemoved(index, itemCount1);
            this.notifyItemRangeChanged(index, this.getItemCount() - itemCount1);
            this.mStructures.remove(groupPosition);
        }

    }

    public void removeHeader(int groupPosition) {
        int index = this.getPositionForGroupHeader(groupPosition);
        if (index >= 0) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            this.notifyItemRemoved(index);
            this.notifyItemRangeChanged(index, this.getItemCount() - index);
            structure.setHasHeader(false);
        }

    }

    public void removeFooter(int groupPosition) {
        int index = this.getPositionForGroupFooter(groupPosition);
        if (index >= 0) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            this.notifyItemRemoved(index);
            this.notifyItemRangeChanged(index, this.getItemCount() - index);
            structure.setHasFooter(false);
        }

    }

    public void removeChild(int groupPosition, int childPosition) {
        int index = this.getPositionForChild(groupPosition, childPosition);
        if (index >= 0) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            this.notifyItemRemoved(index);
            this.notifyItemRangeChanged(index, this.getItemCount() - index);
            structure.setChildrenCount(structure.getChildrenCount() - 1);
        }

    }

    public void removeRangeChild(int groupPosition, int childPosition, int count) {
        if (groupPosition < this.mStructures.size()) {
            int index = this.getPositionForChild(groupPosition, childPosition);
            if (index >= 0) {
                GroupStructure structure = this.mStructures.get(groupPosition);
                int childCount = structure.getChildrenCount();
                int removeCount = count;
                if (childCount < childPosition + count) {
                    removeCount = childCount - childPosition;
                }

                this.notifyItemRangeRemoved(index, removeCount);
                this.notifyItemRangeChanged(index, this.getItemCount() - removeCount);
                structure.setChildrenCount(childCount - removeCount);
            }
        }

    }

    public void removeChildren(int groupPosition) {
        if (groupPosition < this.mStructures.size()) {
            int index = this.getPositionForChild(groupPosition, 0);
            if (index >= 0) {
                GroupStructure structure = this.mStructures.get(groupPosition);
                int itemCount = structure.getChildrenCount();
                this.notifyItemRangeRemoved(index, itemCount);
                this.notifyItemRangeChanged(index, this.getItemCount() - itemCount);
                structure.setChildrenCount(0);
            }
        }

    }

    public void insertGroup(int groupPosition) {
        GroupStructure structure = new GroupStructure(this.hasHeader(groupPosition), this
                .hasFooter(groupPosition), this.getChildrenCount(groupPosition));
        if (groupPosition < this.mStructures.size()) {
            this.mStructures.add(groupPosition, structure);
        } else {
            this.mStructures.add(structure);
            groupPosition = this.mStructures.size() - 1;
        }

        int index = this.countGroupRangeItem(0, groupPosition);
        int itemCount = this.countGroupItem(groupPosition);
        if (itemCount > 0) {
            this.notifyItemRangeInserted(index, itemCount);
            this.notifyItemRangeChanged(index + itemCount, this.getItemCount() - index);
        }

    }

    public void insertRangeGroup(int groupPosition, int count) {
        ArrayList list = new ArrayList();

        int index;
        for (index = groupPosition; index < count; ++index) {
            GroupStructure itemCount = new GroupStructure(this.hasHeader(index), this.hasFooter
                    (index), this.getChildrenCount(index));
            list.add(itemCount);
        }

        if (groupPosition < this.mStructures.size()) {
            this.mStructures.addAll(groupPosition, list);
        } else {
            this.mStructures.addAll(list);
            groupPosition = this.mStructures.size() - list.size();
        }

        index = this.countGroupRangeItem(0, groupPosition);
        int var6 = this.countGroupRangeItem(groupPosition, count);
        if (var6 > 0) {
            this.notifyItemRangeInserted(index, var6);
            this.notifyItemRangeChanged(index + var6, this.getItemCount() - index);
        }

    }

    public void insertHeader(int groupPosition) {
        if (groupPosition < this.mStructures.size() && 0 > this.getPositionForGroupHeader
                (groupPosition)) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            structure.setHasHeader(true);
            int index = this.countGroupRangeItem(0, groupPosition);
            this.notifyItemInserted(index);
            this.notifyItemRangeChanged(index + 1, this.getItemCount() - index);
        }

    }

    public void insertFooter(int groupPosition) {
        if (groupPosition < this.mStructures.size() && 0 > this.getPositionForGroupFooter
                (groupPosition)) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            structure.setHasFooter(true);
            int index = this.countGroupRangeItem(0, groupPosition + 1);
            this.notifyItemInserted(index);
            this.notifyItemRangeChanged(index + 1, this.getItemCount() - index);
        }

    }

    public void insertChild(int groupPosition, int childPosition) {
        if (groupPosition < this.mStructures.size()) {
            GroupStructure structure = this.mStructures.get(groupPosition);
            int index = this.getPositionForChild(groupPosition, childPosition);
            if (index < 0) {
                index = this.countGroupRangeItem(0, groupPosition);
                index += structure.hasHeader() ? 1 : 0;
                index += structure.getChildrenCount();
            }

            structure.setChildrenCount(structure.getChildrenCount() + 1);
            this.notifyItemInserted(index);
            this.notifyItemRangeChanged(index + 1, this.getItemCount() - index);
        }

    }

    public void insertRangeChild(int groupPosition, int childPosition, int count) {
        if (groupPosition < this.mStructures.size()) {
            int index = this.countGroupRangeItem(0, groupPosition);
            GroupStructure structure = this.mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                ++index;
            }

            if (childPosition < structure.getChildrenCount()) {
                index += childPosition;
            } else {
                index += structure.getChildrenCount();
            }

            if (count > 0) {
                structure.setChildrenCount(structure.getChildrenCount() + count);
                this.notifyItemRangeInserted(index, count);
                this.notifyItemRangeChanged(index + count, this.getItemCount() - index);
            }
        }

    }

    public void insertChildren(int groupPosition) {
        if (groupPosition < this.mStructures.size()) {
            int index = this.countGroupRangeItem(0, groupPosition);
            GroupStructure structure = this.mStructures.get(groupPosition);
            if (structure.hasHeader()) {
                ++index;
            }

            int itemCount = this.getChildrenCount(groupPosition);
            if (itemCount > 0) {
                structure.setChildrenCount(itemCount);
                this.notifyItemRangeInserted(index, itemCount);
                this.notifyItemRangeChanged(index + itemCount, this.getItemCount() - index);
            }
        }

    }

    public abstract int getGroupCount();

    public abstract int getChildrenCount(int var1);

    public abstract boolean hasHeader(int var1);

    public abstract boolean hasFooter(int var1);

    public abstract int getHeaderLayout(int var1);

    public abstract int getFooterLayout(int var1);

    public abstract int getChildLayout(int var1);

    protected abstract void onViewHolderCreated(CacheViewHolder viewHolder, int viewType);

    protected abstract void onBindHeaderViewHolder(CacheViewHolder var1, int var2);

    protected abstract void onBindFooterViewHolder(CacheViewHolder var1, int var2);

    protected abstract void onBindChildViewHolder(CacheViewHolder var1, int var2, int var3);

    class GroupDataObserver extends RecyclerView.AdapterDataObserver {
        GroupDataObserver() {
        }

        public void onChanged() {
            GroupedRecyclerViewAdapter.this.isDataChanged = true;
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            GroupedRecyclerViewAdapter.this.isDataChanged = true;
        }

        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            this.onItemRangeChanged(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            GroupedRecyclerViewAdapter.this.isDataChanged = true;
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            GroupedRecyclerViewAdapter.this.isDataChanged = true;
        }
    }
}
