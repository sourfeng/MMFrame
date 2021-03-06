package com.android.ww.wwframe.adapter.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.ww.wwframe.R;
import com.android.ww.wwframe.base.BaseFragment;

import java.util.List;

/**
 * Initialization the menu view. handle the menu event.
 */
public class MenuTabAdapter implements OnClickListener, View.OnTouchListener {

    private List<Fragment> mMenuFragment;
    private List<View> mMenuView;
    private FragmentActivity mFragmentActivity;
    private int mFragmentContentId;
    private int mCurrentMenu = -1;
    private boolean mAnimationFlag = false;
    private OnMenuClickListener mMenuClickListener;

    private MyGestureDetector gestureDetector;

    /**
     * @param activity          fragment activity
     * @param view              menu view list
     * @param list              menu fragment list
     * @param fragmentContentId main fragment content
     */
    public MenuTabAdapter(FragmentActivity activity, List<View> view,
                          List<Fragment> list, int fragmentContentId) {
        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick(gestureDetector.getView());
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mMenuClickListener != null) {
                    mMenuClickListener.onDoubleClick(gestureDetector.getView());
                }
                return super.onDoubleTap(e);
            }
        };
        this.gestureDetector = new MyGestureDetector(activity, listener);
        this.gestureDetector.setOnDoubleTapListener(listener);

        this.mMenuFragment = list;
        this.mMenuView = view;
        this.mFragmentActivity = activity;
        this.mFragmentContentId = fragmentContentId;

        // add this first fragment to default
        // FragmentTransaction ft = this.mFragmentActivity
        // .getSupportFragmentManager().beginTransaction();
        // ft.add(fragmentContentId, this.mMenuFragment.get(0), "home");
        // ft.commit();

        int menuSize = this.mMenuView.size();
        for (int i = 0; i < menuSize; i++) {
            View v = this.mMenuView.get(i);
            v.setClickable(true);
            v.setOnTouchListener(this);
            this.mMenuView.get(i).setOnClickListener(this);
        }
        // changeMenuStatus(0);
//        onClick(this.mMenuView.get(0));
    }


    public void setOnMenuClickListener(OnMenuClickListener listener) {
        this.mMenuClickListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.setView(v);
        return gestureDetector.onTouchEvent(event);
    }


    @Override
    public void onClick(View v) {
        Fragment fragment = getCurrentMenuFragment();
        if (fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onKeyDown(KeyEvent.KEYCODE_BACK, null);
        }

        if (mMenuClickListener != null) {
            mMenuClickListener.onClick(v);
        }

        switch (v.getId()) {
//            case R.id.tab_tour:// home fragment
//                if (mCurrentMenu == 0) {
//                    return;
//                }
//                changeMenuStatus(0);
//                changeMenu(0);
//                break;
//            case R.id.tab_flaw:// search fragment
//                if (mCurrentMenu == 1) {
//                    return;
//                }
//                changeMenuStatus(1);
//                changeMenu(1);
//                break;
//            case R.id.tab_supervise:// message fragment
//                if (mCurrentMenu == 2) {
//                    return;
//                }
//
//                changeMenuStatus(2);
//                changeMenu(2);
//                break;
//            case R.id.tab_account:
//                if (mCurrentMenu == 3) {
//                    return;
//                }
//                changeMenuStatus(3);
//                changeMenu(3);
//                break;
//            case R.id.tab_data_user:// manage fragment
//                if (mCurrentMenu == 4) {
//                    return;
//                }
//                changeMenuStatus(4);
//                changeMenu(4);
//                break;
//            case R.id.tab_data_line:// setting fragment
//                if (mCurrentMenu == 5) {
//                    return;
//                }
//                changeMenuStatus(5);
//                changeMenu(5);
//                break;
//            case R.id.tab_data_flaw:// setting fragment
//                if (mCurrentMenu == 6) {
//                    return;
//                }
//                changeMenuStatus(6);
//                changeMenu(6);
//                break;
        }
    }

    public void change(int index) {
        if (mCurrentMenu == index)
            return;
        changeMenuStatus(index);
        changeMenu(index);
    }

    /**
     * change the menu status.
     *
     * @param index the menu view list index
     */
    public void changeMenuStatus(int index) {
        int menuSize = this.mMenuView.size();
        for (int i = 0; i < menuSize; i++) {
            if (i == index) {
                this.mMenuView.get(i).setSelected(true);
            } else {
                this.mMenuView.get(i).setSelected(false);
            }
        }
    }

    public void changeMenuStatus(){
        int menuSize = this.mMenuView.size();
        for (int i = 0; i < menuSize; i++) {
            this.mMenuView.get(i).setSelected(false);
        }
    }

    /**
     * when click the menu button,if the view has been added in the fragment ,
     * it will be exec method onResume(),then show it, if not,it will be added
     * into the fragment and show it.
     *
     * @param index the menu view list index
     */
    public void changeMenu(int index) {
        Fragment fragment = this.mMenuFragment.get(index);
        FragmentTransaction ft = obtainFragmentTransaction(index);

        Fragment currFragment = getCurrentMenuFragment();
        if (currFragment != null) {
            currFragment.setUserVisibleHint(false);
            currFragment.onPause();
        }

        if (fragment.isAdded()) {
            fragment.setUserVisibleHint(true);
            fragment.onResume();
        } else {
            String tag = "";
            switch (index) {
                case 0:
                    tag = "tour";
                    break;
                case 1:
                    tag = "flaw";
                    break;
                case 2:
                    tag = "supervise";
                    break;
                case 3:
                    tag = "account";
                    break;
                case 4:
                    tag = "data_user";
                    break;
                case 5:
                    tag = "data_line";
                    break;
                case 6:
                    tag ="data_flaw";
                    break;
            }
            ft.add(mFragmentContentId, fragment, "frag_tag_" + index);
            fragment.setUserVisibleHint(true);
        }
        showMenuContent(index);
        ft.commit();
    }

    /**
     * show the selected fragment and hide others
     *
     * @param index the menu view list index
     */
    private void showMenuContent(int index) {
        int size = this.mMenuFragment.size();
        for (int i = 0; i < size; i++) {
            Fragment fragment = this.mMenuFragment.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(i);

            if (index == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        mCurrentMenu = index; // set current menu
    }

    /**
     * set animation flag.
     *
     * @param b boolean
     */
    public void setIsAnimation(boolean b) {
        this.mAnimationFlag = b;
    }

    /**
     * if the mAnimationFlag is true,when two fragment are exchanging,it will
     * have animation.
     *
     * @param index the menu view list index
     * @return object of FragmentTransaction
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = this.mFragmentActivity
                .getSupportFragmentManager().beginTransaction();
        if (this.mAnimationFlag) {
            // set animation
            if (index > mCurrentMenu) {
                ft.setCustomAnimations(R.anim.slide_left_in,
                        R.anim.slide_left_out);
            } else {
                ft.setCustomAnimations(R.anim.slide_right_in,
                        R.anim.slide_right_out);
            }
        }

        return ft;
    }

    /**
     * get the id which have been selected
     *
     * @return the selected index of the menu list
     */
    public int getCurrentMenu() {
        return mCurrentMenu;
    }

    /**
     * get the fragment which have been selected
     *
     * @return the selected fragment
     */
    public Fragment getCurrentMenuFragment() {
        if (mCurrentMenu < 0 || mCurrentMenu >= mMenuFragment.size()) {
            return null;
        }
        return this.mMenuFragment.get(mCurrentMenu);
    }

    public Fragment getMenuFragment(int i) {
        return this.mMenuFragment.get(i);
    }

    class MyGestureDetector extends GestureDetector {
        View view;

        public MyGestureDetector(Context context, OnGestureListener listener) {
            super(context, listener);
        }

        public void setView(View vi) {
            view = vi;
        }

        public View getView() {
            return view;
        }
    }

    public interface OnMenuClickListener extends OnClickListener {
        void onDoubleClick(View view);
    }
}
