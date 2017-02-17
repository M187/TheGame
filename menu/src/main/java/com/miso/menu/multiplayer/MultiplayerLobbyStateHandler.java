package com.miso.menu.multiplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.miso.menu.MultiplayerLobby;
import com.miso.menu.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by michal.hornak on 10.05.2016.
 */
public class MultiplayerLobbyStateHandler implements RecyclerViewAdapter.MyClickListener{

    private MultiplayerLobby multiplayerLobby;

    private Button mJoinButton;
    private Button mMainButton1;
    private Button mMainButton2;
    private EditText mPlayerNickname;
    private TextView mTextInfo;
    private RecyclerView mColorRecyclerView;
    private ImageView mColorPreview;

    @Override
    public void onItemClick(int position, View view) {
        this.mColorPreview.setBackgroundColor(PlayerColors.getAllColors().get(position));
    }

    public enum LobbyState {
        Default,
        Joined,
        JoinedAndReadyForGame,
        Hosting
    }

    public MultiplayerLobbyStateHandler(MultiplayerLobby multiplayerLobby) {
        this.multiplayerLobby = multiplayerLobby;
        bindViews();
    }

    private void bindViews() {
        this.mJoinButton = ((Button) this.multiplayerLobby.findViewById(R.id.button_join));
        this.mMainButton1 = ((Button) this.multiplayerLobby.findViewById(R.id.button_main_1));
        this.mMainButton2 = ((Button) this.multiplayerLobby.findViewById(R.id.button_main_2));
        this.mPlayerNickname = ((EditText) this.multiplayerLobby.findViewById(R.id.player_nickname));
        this.mTextInfo = ((TextView) this.multiplayerLobby.findViewById(R.id.text_info_game_state_events));
        this.mColorRecyclerView = (RecyclerView) multiplayerLobby.findViewById(R.id.color_list);
        this.mColorPreview = (ImageView) multiplayerLobby.findViewById(R.id.chosen_color_preview);
        intializeColors();
    }

    public void joinClickUiEvents() {
        this.mJoinButton.setEnabled(false);
        this.mMainButton1.setEnabled(false);
        this.mPlayerNickname.setEnabled(false);

        this.mTextInfo.setText(R.string.join_message);
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));

        bindJoinEventsToMainButtons();
        hideJoinRowAnimation();
    }

    public void abandonClickUiEvents() {
        this.mTextInfo.setText(R.string.abandon_message);
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));
        this.mJoinButton.setEnabled(true);
        this.mMainButton1.setEnabled(false);
        this.mMainButton2.setEnabled(false);
        this.mPlayerNickname.setEnabled(true);

        unbindJoinEventsToMainButtons();
        showJoinRowAnimation();
    }

    public void hostClickUiChanges() {
        this.mTextInfo.setText(R.string.host_message);
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_red_dark));
        this.mMainButton1.setText(R.string.unhost);
        this.mMainButton1.setEnabled(false);
        this.mJoinButton.setEnabled(false);
        this.mPlayerNickname.setEnabled(false);

        hideJoinRowAnimation();
    }

    public void unHostClickUiChanges() {
        this.mTextInfo.setText(R.string.unhost_message);
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));
        this.mMainButton1.setText(R.string.host);
        this.mMainButton1.setEnabled(false);
        this.mJoinButton.setEnabled(true);
        this.mMainButton2.setEnabled(false);
        this.mPlayerNickname.setEnabled(true);

        showJoinRowAnimation();
    }

    public void readyClickUiChanges() {
        this.mMainButton2.setText(R.string.unready);
    }

    public void unReadyClickChanges() {
        this.mMainButton2.setText(R.string.ready);
    }

    private void showJoinRowAnimation() {
        this.multiplayerLobby.findViewById(R.id.joined_players_row)
                .animate()
                .translationY(this.multiplayerLobby.findViewById(R.id.join_game_row).getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {

                        multiplayerLobby.findViewById(R.id.joined_players_row).setTranslationY(0);
                        multiplayerLobby.findViewById(R.id.join_game_row).setVisibility(View.VISIBLE);
                        multiplayerLobby.findViewById(R.id.join_game_row)
                                .animate()
                                .translationX(0)
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mMainButton1.setEnabled(true);
                                        multiplayerLobby.findViewById(R.id.multiplayer_lobby_layout).invalidate();
                                    }
                                });
                    }
                });
    }

    private void hideJoinRowAnimation() {
        this.multiplayerLobby.findViewById(R.id.join_game_row)
                .animate()
                .translationX(-this.multiplayerLobby.findViewById(R.id.join_game_row).getWidth())
                .setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        multiplayerLobby.findViewById(R.id.joined_players_row)
                                .animate()
                                .translationY(-multiplayerLobby.findViewById(R.id.join_game_row).getHeight())
                                .setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        mMainButton2.setEnabled(true);
                                        mMainButton1.setEnabled(true);
                                        multiplayerLobby.findViewById(R.id.joined_players_row).setTranslationY(0);
                                        multiplayerLobby.findViewById(R.id.join_game_row).setVisibility(View.GONE);
                                        multiplayerLobby.findViewById(R.id.multiplayer_lobby_layout).invalidate();
                                    }
                                });
                    }
                });
    }

    private void bindJoinEventsToMainButtons() {
        this.mMainButton1.setText(R.string.abandon);
        this.mMainButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplayerLobby.abandonClick(v);
            }
        });

        this.mMainButton2.setText(R.string.ready);
        this.mMainButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplayerLobby.readyClick(v);
            }
        });
    }

    private void unbindJoinEventsToMainButtons() {
        this.mMainButton1.setText(R.string.host);
        this.mMainButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplayerLobby.hostClick(v);
            }
        });

        this.mMainButton2.setText(R.string.start);
        this.mMainButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplayerLobby.startGame(v);
            }
        });
    }

    public void intializeColors() {

        List<Integer> rowListItem = PlayerColors.getAllColors();
        GridLayoutManager manager = new GridLayoutManager(multiplayerLobby, 2);

        mColorRecyclerView.setLayoutManager(manager);
        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(multiplayerLobby, rowListItem, this);

        int spacingInPixels = multiplayerLobby.getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        mColorRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true, 0));

        mColorRecyclerView.setAdapter(rcAdapter);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;
        private int headerNum;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge, int headerNum) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
            this.headerNum = headerNum;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view) - headerNum; // item position

            if (position >= 0) {
                int column = position % spanCount; // item column

                if (includeEdge) {
                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                    outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                    if (position >= spanCount) {
                        outRect.top = spacing; // item top
                    }
                }
            } else {
                outRect.left = 0;
                outRect.right = 0;
                outRect.top = 0;
                outRect.bottom = 0;
            }
        }
    }
}
