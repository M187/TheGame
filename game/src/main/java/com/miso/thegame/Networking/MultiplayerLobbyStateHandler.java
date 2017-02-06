package com.miso.thegame.Networking;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.miso.thegame.MultiplayerLobby;
import com.miso.thegame.R;
import com.miso.thegame.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by michal.hornak on 10.05.2016.
 */
public class MultiplayerLobbyStateHandler {

    private MultiplayerLobby multiplayerLobby;

    @BindView(R2.id.button_join)
    Button mJoinButton;
    @BindView(R2.id.button_ready)
    Button mReadyButton;
    @BindView(R2.id.button_abandon)
    Button mAbandonButton;
    @BindView(R2.id.button_host)
    Button mHostButton;
    @BindView(R2.id.button_start)
    Button mStartButton;
    @BindView(R2.id.player_nickname)
    EditText mPlayerNickname;
    @BindView(R2.id.textinfo_game_state_events)
    TextView mTextInfo;


    public MultiplayerLobbyStateHandler(MultiplayerLobby multiplayerLobby){
        this.multiplayerLobby = multiplayerLobby;
        ButterKnife.bind(multiplayerLobby);
    }

    public void joinClickUiEvents(){
        this.mJoinButton.setEnabled(false);
        this.mReadyButton.setEnabled(true);
        this.mAbandonButton.setEnabled(true);
        this.mHostButton.setEnabled(false);
        this.mPlayerNickname.setEnabled(false);

        this.mTextInfo.setText("Join successful!");
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));
    }

    public void abandonClickUiEvents(){
        this.mJoinButton.setEnabled(true);
        this.mReadyButton.setEnabled(false);
        this.mAbandonButton.setEnabled(false);
        this.mHostButton.setEnabled(true);
        this.mPlayerNickname.setEnabled(true);
    }

    public String hostClickUiChanges(){
        this.mTextInfo.setText("Hosting Game!");
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_red_dark));
        this.mHostButton.setText("UN-HOST");
        this.mJoinButton.setEnabled(false);
        this.mStartButton.setEnabled(true);
        this.mPlayerNickname.setEnabled(false);
        (this.multiplayerLobby.findViewById(R.id.join_game_row)).setVisibility(View.INVISIBLE);

        return this.mPlayerNickname.getText().toString();
    }

    public void unHostClickUiChanges(){
        this.mTextInfo.setText("Not hosting any game!");
        this.mTextInfo.setTextColor(this.multiplayerLobby.getResources().getColor(android.R.color.holo_green_dark));
        this.mHostButton.setText("HOST");
        this.mJoinButton.setEnabled(true);
        this.mStartButton.setEnabled(false);
        this.mPlayerNickname.setEnabled(true);
        (this.multiplayerLobby.findViewById(R.id.join_game_row)).setVisibility(View.VISIBLE);
    }

    public void readyClickUiChanges(){
        this.mReadyButton.setText("UN-READY");
    }

    public void unReadyClickChanges(){
        this.mReadyButton.setText("READY");
    }

    public enum LobbyState{
        Default,
        Joined,
        JoinedAndReadyForGame,
        Hosting
    }
}
