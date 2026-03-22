package View.Dialog;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import Entity.Pair;

/**
  * 共通ダイアログクラス
  */
public class CommonDialogView extends JDialog
{
    /* 共通ダイアログ表示文言テーブル（共通ダイアログ種別、ダイアログタイトル、ダイアログ本文） */
    private final Map<CommonDialogType, Pair<String, String>> MessageTable = new HashMap<CommonDialogType, Pair<String, String>>()
    {
        {
            put(CommonDialogType.DBConnectionFailureDialog, new Pair<String, String>("DB接続失敗ダイアログ", "DBの接続に失敗しました"));
            put(CommonDialogType.LoginFailureDialog, new Pair<String, String>("ログイン失敗ダイアログ", "ログイン失敗です"));
            put(CommonDialogType.SiginuPFailureDialog, new Pair<String, String>("登録失敗ダイアログ", "登録失敗です"));
            put(CommonDialogType.InputContentFailureDialog, new Pair<String, String>("System failure", "入力テキストが間違っています。"));
            put(CommonDialogType.ListLengthFailureDialog, new Pair<String, String>("System failure", "リスト名は0文字以上10文字以下で設定してください"));
            put(CommonDialogType.ListDeleteFailureDialog, new Pair<String, String>("失敗ダイアログ", "リスト削除失敗です"));
            put(CommonDialogType.ListUpdateFailureDialog, new Pair<String, String>("失敗ダイアログ", "リスト更新失敗です"));
            put(CommonDialogType.TaskDeleteFailureDialog, new Pair<String, String>("失敗ダイアログ", "タスク削除失敗です"));
            put(CommonDialogType.TaskCreateFailureDialog, new Pair<String, String>("System failure", "登録失敗です。"));
            put(CommonDialogType.TaskUpdateFailureDialog, new Pair<String, String>("編集失敗ダイアログ", "編集失敗です。"));
            put(CommonDialogType.GetTaskFailureDialog, new Pair<String, String>("System failure", "タスクの取得に失敗しました。"));
            put(CommonDialogType.GetUserListFailureDialog, new Pair<String, String>("System failure", "リスト情報の取得に失敗しました。"));
            put(CommonDialogType.GetListAndTaskFailureDialog, new Pair<String, String>("System failure", "リスト内容またはタスク内容の取得に失敗しました。"));
            put(CommonDialogType.UpdateTaskFailureDialog, new Pair<String, String>("System failure", "タスク情報を編集できませんでした。"));
            put(CommonDialogType.ListTaskCreateSuccessDialog, new Pair<String, String>("登録成功ダイアログ", "登録成功です。"));
            put(CommonDialogType.ListTaskCreateFailDialog, new Pair<String, String>("登録失敗ダイアログ", "登録失敗しました。再度実行してください。"));
            put(CommonDialogType.AskFailDialog, new Pair<String, String>("生成失敗ダイアログ", "生成失敗しました。再度実行してください。"));
        }
    };
    
    /* 共通ダイアログの横幅 */
    private final int DialogWidth = 400;

    /* 共通ダイアログの高さ */
    private final int DialogHeight = 200;

    /* 共通ダイアログ文言の横幅 */
    private final int OperationWidth = 350;

    /* 共通ダイアログ文言の高さ */
    private final int OperationHeight = 100;

    /* 共通ダイアログ文言の表示X座標 */
    private final int OperationPositionX = 20;

    /* 共通ダイアログ文言の表示Y座標 */
    private final int OperationPositionY = 20;

    /* ダイアログ表示文言 */
    private JLabel OperationLabel;

    /**
     * コンストラクタ
     * @param ownerView 親JFrameインスタンス
     */
    public CommonDialogView(JFrame ownerView)
    {
        // JDialogクラスを初期化
        super(ownerView);
        this.setSize(this.DialogWidth, this.DialogHeight);
        this.setLocationRelativeTo(null);
        this.setLayout(null);

        // TODO: 表示位置も可変にしたいなら、文言と同様にテーブル化する。
        this.OperationLabel = new JLabel();
        this.OperationLabel.setBounds(this.OperationPositionX, this.OperationPositionY, this.OperationWidth, this.OperationHeight);
        this.add(this.OperationLabel);

        // TODO: ボタンも表示するなら追加する。YesとNo（閉じると共用、文言切替で対応）で2^2=4パターンの列挙体を定義し表示切替できるように対応する
    }

    /* 共通ダイアログ種別列挙体 */
    public enum CommonDialogType
    {
        /* ログイン失敗ダイアログ */
        DBConnectionFailureDialog,
        LoginFailureDialog,
        SiginuPFailureDialog,
        InputContentFailureDialog,
        ListLengthFailureDialog,
        ListDeleteFailureDialog,
        ListUpdateFailureDialog,
        TaskDeleteFailureDialog,
        TaskUpdateFailureDialog,
        TaskCreateFailureDialog,
        GetTaskFailureDialog,
        GetUserListFailureDialog,
        GetListAndTaskFailureDialog,
        UpdateTaskFailureDialog,

        /* 登録成功ダイアログ */
        ListTaskCreateSuccessDialog,

        /* 登録失敗ダイアログ */
        ListTaskCreateFailDialog,

        /** 生成失敗ダイアログ */
        AskFailDialog;
    }

    /**
     * ダイアログ表示する
     * @param dialogType 表示するダイアログ種別
     * @param isModal モーダル表示とするか（ダイアログ表示中は画面スレッドがロックするため注意）
     */
    public void Show(CommonDialogType dialogType, boolean isModal)
    {
        if (this.MessageTable.containsKey(dialogType))
        {
            // テーブルから文言を取得して代入する
            var search = this.MessageTable.get(dialogType);
            this.setTitle(search.Value1);

            // TODO: 改行に対応するなら、テーブルには改行文字を含む文字列で登録し代入する際に処理する
            this.OperationLabel.setText(search.Value2);
        }
        else
        {
            // テーブルに登録がない場合は、空文字を代入する
            this.setTitle("");
            this.OperationLabel.setText("");
        }

        // TODO: ボタンを追加するなら押下監視開始する

        // ダイアログ表示指示
        this.setModal(isModal);
        this.setVisible(true);
    }

    /**
     * ダイアログ閉じる
     * モーダルの場合は画面スレッドがロックするため外部からのクローズは不可
     */
    public void Hide()
    {
        // ダイアログ非表示指示
        this.setVisible(false);

        // TODO: ボタンを追加するなら押下監視終了する

        // 表示文言をクリアする
        this.setModal(false);
        this.setTitle("");
        this.OperationLabel.setText("");
    }
}
