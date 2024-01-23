# EngineersGate Webアプリケーションオリジナル課題2

Webアプリケーションオリジナル課題2「SNSサイト」の制作物になります。


## 目的

Webアプリケーションオリジナル課題2「SNSサイト」の制作物として、
下記を実装しました。

- アカウント作成ページ
- ログインページ
- ダッシュボードページ
- プロフィールページ
- フレンドページ
- エラーページ


## 設計

使用した技術、実行環境及び設計は以下になります。

### 実行環境

- OS：Windows10
- IDE：Eclipse 2022-12
- Java：1.8.0_231
- SpringBoot：3.0.4
    - その他参照ライブラリは [build.gradle](https://github.com/hide-mouri/engineers_gate_springboot/blob/main/build.gradle) 参照
- MySQL：8.0.32

### DB設計

![ER図](docs/erd/eg_sns_erd.png)

### パッケージ構成

```
src
└─main
    ├─java
    │  └─jp
    │      └─dcworks
    │          └─engineersgate
    │              └─egsns
    │                  ├─controller      # コントローラクラス
    │                  ├─core            # コアクラス。アプリ基底処理及び、設定に関する処理。
    │                  │  └─annotation
    │                  ├─dto             # DTOクラス。入力フォーム関連。
    │                  ├─entity          # DBエンティティクラス。
    │                  ├─repository      # DBアクセス。
    │                  ├─service         # リポジトリをラップしたサービスクラス。他、必要に応じて外部連携等コンポーネント。
    │                  └─util            # ユーティリティクラス。
    └─resources
        ├─static                         # 静的ファイル。js、css等。
        │  └─assets
        └─templates                      # テンプレートファイル。
           ├─account
           ├─common
           ├─error
           ├─friend
           ├─home
           ├─login
           └─profile
```


## 画面説明

### ログイン画面

ログイン機能を提供します。

<img src="docs/screenshot/login/index_001.jpg" width="75%">

- 「ログインID」「パスワード」を入力し、ログインします。
- 「ログインID」「パスワード」は必須入力です。

<img src="docs/screenshot/login/index_002.jpg" width="75%">

- 「ログインID」「パスワード」を入力しなかった場合、jsでバリデーションを行いエラーとします。

<img src="docs/screenshot/login/index_003.jpg" width="75%">

- ユーザーが存在しない場合は、サーバサイドでバリデーションを行います。





