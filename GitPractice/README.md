# Git 練習筆記（GitPractice）

> 這個資料夾是我的 Git 練習場，也是記事本，放在 ProjectTest2026 儲存庫（今年所有練習）底下。
> 每做一個操作，就把「指令、意義、實際看到的結果、心得」記在這裡。
> 目標：不再發生「目標分支選錯、混進不相關檔案、別人 clone 下來無法編譯」這三種錯誤。

---

## 目錄

- [0. 記錄格式](#0-記錄格式)
- [1. 核心觀念](#1-核心觀念)
- [2. 環境設定（只做一次）](#2-環境設定只做一次)
- [3. Day 1：把 GitPractice 加入 ProjectTest2026（第一次完整 PR 流程）](#3-day-1把-gitpractice-加入-projecttest2026第一次完整-pr-流程)
- [4. 練習 1：目標分支選錯](#4-練習-1目標分支選錯)
- [5. 練習 2：混進不相關的檔案](#5-練習-2混進不相關的檔案)
- [6. 練習 3：別人 clone 下來是壞的](#6-練習-3別人-clone-下來是壞的)
- [7. 送出 PR 前的檢查清單](#7-送出-pr-前的檢查清單)
- [8. 補救指令速查](#8-補救指令速查)
- [9. 名詞解釋](#9-名詞解釋)
- [10. 我的錯誤與心得紀錄](#10-我的錯誤與心得紀錄)

---

## 0. 記錄格式

每個操作都用這個格式記錄：

```
### 步驟名稱
- 指令：`git xxx`
- 意義：這個指令做了什麼、為什麼要做
- 實際結果：我執行後看到什麼（可以貼上終端機的輸出）
- 心得／踩到的坑：
```

---

## 1. 核心觀念

### 1.1 檔案的四個位置

```
工作目錄            暫存區（Staging）       本地儲存庫             遠端儲存庫（GitHub）
（正在修改的檔案）  ──git add──▶ （準備提交）──git commit──▶ （本機的紀錄）──git push──▶（大家共用）
                                                            ◀──git fetch / git pull──
```

- **工作目錄**：電腦上實際看到、正在修改的檔案。
- **暫存區**：用 `git add` 挑選「這次要提交哪些修改」。
- **本地儲存庫**：`git commit` 之後的紀錄，只存在自己電腦上。
- **遠端儲存庫**：`git push` 之後，別人才看得到。

### 1.2 分支只是一個「指標」

- 分支是指向某個 commit 的標籤，切換分支就是移動到另一個標籤。
- `HEAD` 代表「我現在在哪裡」。

### 1.3 `origin/main` 是什麼

- 代表「上次 `git fetch` 時，遠端 `main` 的狀態」。
- 它**不會自動更新**，要先 `git fetch` 才會是最新的。

### 1.4 PR 的內容是怎麼決定的

- PR 的內容 = **「我的分支」和「目標分支」之間的差異**。
- 所以「從哪個分支切出來」和「PR 要合到哪個分支」必須一致，否則會帶進別人的 commit。

---

## 2. 環境設定（只做一次）

### 2.1 設定身分

- 指令：
  ```powershell
  git config --global user.name "我的 GitHub 帳號名稱"
  git config --global user.email "我的 GitHub noreply 信箱"
  ```
- 意義：每個 commit 都會記錄作者。使用 GitHub 的 noreply 信箱（GitHub → Settings → Emails → Keep my email addresses private）可以保護隱私，不會公開真實信箱。
- 實際結果：
- 心得：

### 2.2 換行符號設定（Windows 必做）

- 指令：`git config --global core.autocrlf true`
- 意義：Windows 用 CRLF 換行、Linux/macOS 用 LF。這個設定讓 Git 在提交時轉成 LF、取出時轉回 CRLF，避免「整份檔案每一行都被標示修改」。
- 補充：這個資料夾裡的 `.gitattributes` 也會統一換行規則，兩者一起使用最保險。
- 實際結果：
- 心得：

### 2.3 預設分支名稱

- 指令：`git config --global init.defaultBranch main`
- 意義：新建的儲存庫預設分支叫 `main`（舊版 Git 預設是 `master`）。
- 實際結果：
- 心得：

### 2.4 確認設定

- 指令：`git config --global --list`
- 意義：列出目前所有的全域設定，確認上面三項都正確。
- 實際結果：
- 心得：

---

## 3. Day 1：把 GitPractice 加入 ProjectTest2026（第一次完整 PR 流程）

> GitPractice 是 ProjectTest2026 儲存庫底下的一個資料夾（ProjectTest2026 存放今年所有的練習）。
> 所以**不需要、也不可以**在 GitPractice 裡執行 `git init`，否則會變成「儲存庫裡面又有一個儲存庫」。
> 這一天的目標：用正確的流程（開分支 → 提交 → PR → 合併），把 GitPractice 加進 ProjectTest2026。

### 3.1 進入儲存庫並查看狀態

- 指令：
  ```powershell
  cd "C:\Users\dmc-admin\Desktop\android test\ProjectTest2026"
  git status
  ```
- 意義：列出哪些檔案有變動、哪些還沒被追蹤（Untracked）。**每次動手之前、提交之前都要先看這個。**
- 預期：只會看到 `GitPractice/` 是 Untracked。如果還看到其他檔案（例如 `.idea/misc.xml`），代表有不相關的修改，先處理掉再繼續（見 3.2）。
- 實際結果：
- 心得：

### 3.2 處理不相關的修改（如果有的話）

- 指令：
  ```powershell
  git diff 檔案路徑          # 先看改了什麼
  git restore 檔案路徑       # 確定不要，就放棄修改 ⚠️ 修改會消失
  ```
- 意義：Android Studio 開啟專案時，常會自動修改 `.idea/` 裡的設定檔（例如在 `misc.xml` 加上 `default="true"`）。這些不是我要提交的內容，混進 commit 就是「不相關的檔案」。
- 實際結果：
- 心得：

### 3.3 從最新的 main 切出功能分支

- 指令：
  ```powershell
  git fetch origin
  git switch -c docs/git-practice-notes origin/main
  ```
- 意義：
  - `git fetch origin`：抓遠端的最新狀態，讓 `origin/main` 是最新的。
  - `git switch -c 新分支 origin/main`：從最新的遠端 main 建立新分支並切換過去。
  - **切出的來源（main）= 等一下 PR 的目標（main）**，PR 才不會帶進別人的 commit。
  - 分支命名：`類型/簡短描述`，例如 `feature/`、`fix/`、`docs/`。
- 實際結果：
- 心得：

### 3.4 只加入這次要提交的檔案

- 指令：
  ```powershell
  git add GitPractice/
  git status
  git diff --staged --stat
  ```
- 意義：
  - 明確指定路徑，只加入 `GitPractice/` 資料夾。**不要用 `git add .`**，它會把所有變動一起加進去。
  - `git diff --staged --stat`：列出「即將被提交」的檔案，預期只有 `GitPractice/.gitattributes`、`GitPractice/.gitignore`、`GitPractice/README.md` 三個。
- 實際結果：
- 心得：

### 3.5 提交

- 指令：`git commit -m "docs: 新增 Git 練習筆記 GitPractice"`
- 意義：把暫存區的內容存成一個 commit。一個 commit 只做一件事。
- commit message 格式（常見規範）：
  - `feat:` 新功能
  - `fix:` 修 bug
  - `docs:` 文件
  - `refactor:` 重構（不改變功能）
  - `chore:` 雜項（設定、建置）
- 實際結果：
- 心得：

### 3.6 送出前自我檢查

- 指令：
  ```powershell
  git log --oneline origin/main..HEAD
  git diff origin/main...HEAD --stat
  ```
- 意義：
  - 第一行列出「我的分支有、main 沒有」的 commit，也就是 PR 會包含的 commit。預期只有 3.5 那一個。
  - 第二行列出 PR 會改到的檔案。預期只有 GitPractice 的三個檔案。
  - **出現不認識的 commit 或檔案，就先停下來，不要推送。**
- 實際結果：
- 心得：

### 3.7 推送分支

- 指令：`git push -u origin docs/git-practice-notes`
- 意義：把分支推到 GitHub；`-u` 設定上游分支，之後在這個分支只要打 `git push`。
- 實際結果：
- 心得：

### 3.8 在 GitHub 開 PR 並合併

- 操作：
  1. GitHub 上的 ProjectTest2026 → 會出現「Compare & pull request」按鈕。
  2. **確認 base 是 `main`、compare 是 `docs/git-practice-notes`**。
  3. 點 Files changed，確認只有 GitPractice 的三個檔案。
  4. 描述寫清楚：改了什麼、為什麼。
  5. Merge pull request → Delete branch。
- 意義：即使是自己的專案，也照團隊流程走 PR，養成「送出前確認目標分支和內容」的習慣。
- 實際結果：
- 心得：

### 3.9 回到 main 並清理本機分支

- 指令：
  ```powershell
  git switch main
  git pull
  git branch -d docs/git-practice-notes
  git log --oneline --graph --all
  ```
- 意義：
  - `git pull`：把剛合併的結果抓回本機的 main。
  - `git branch -d`：刪除已經合併的本機分支（沒合併的分支會拒絕刪除，比較安全）。
  - `git log --oneline --graph --all`：用分支圖觀察歷史。
- 實際結果：
- 心得：

### 3.10 建立 develop 分支並設定保護（為練習 1 做準備）

- 指令：
  ```powershell
  git switch -c develop
  git push -u origin develop
  ```
- GitHub 網頁操作：
  1. Settings → Branches → 對 `main` 和 `develop` 新增規則，勾選 **Require a pull request before merging**。
  2. （可選）Settings → General → Default branch 改成 `develop`。
- 意義：
  - 很多團隊用 `main` 放正式版本、`develop` 放開發中的版本，功能分支從 `develop` 切出、PR 合回 `develop`。
  - 分支保護：禁止直接推送到主線，所有修改都必須透過 PR，模擬公司的流程。
  - 預設分支改成 `develop`：開 PR 時預設目標就是 `develop`，降低選錯目標分支的機會。
- 實際結果：
- 心得：

---

## 4. 練習 1：目標分支選錯

> 目的：親眼看到「切出分支的來源」和「PR 的目標」不一致時，PR 會帶進別人的 commit。

步驟（練習時逐步填寫）：

1. 在 `develop` 上建立 2～3 個 commit（模擬同事已經合併、但還沒進 `main` 的功能）。
2. 從 `develop` 切出 `feature/a`，做一個小修改並推送。
3. **故意把 PR 的目標設成 `main`**，觀察 PR 的 Commits 和 Files changed。
4. 在本機用 `git log --oneline origin/main..HEAD` 確認同樣的現象。
5. 把 PR 的目標改成 `develop`，確認只剩下自己的修改。

關鍵指令：

| 指令 | 意義 |
|---|---|
| `git fetch origin` | 抓最新的遠端狀態 |
| `git switch -c feature/a origin/develop` | 從最新的遠端 develop 切出新分支 |
| `git log --oneline origin/develop..HEAD` | 列出「我的分支有、目標分支沒有」的 commit，也就是 PR 會包含的 commit |
| `git diff origin/develop...HEAD --stat` | 列出 PR 會改到的檔案 |

紀錄：

---

## 5. 練習 2：混進不相關的檔案

> 目的：養成提交前檢查 `git status` 和 `git diff --staged` 的習慣。

步驟（練習時逐步填寫）：

1. 同時修改兩個檔案，但這次只想提交其中一個。
2. 用 `git status` 觀察。
3. 只加入要提交的檔案；練習 `git add -p` 逐段挑選。
4. 加錯了，用 `git restore --staged 檔案` 移出暫存區。
5. 模擬已經推上去：把一個不該上傳的檔案推上去，再用 `git rm --cached` 加 `.gitignore` 修正。

關鍵指令：

| 指令 | 意義 |
|---|---|
| `git add -p` | 逐段（hunk）詢問要不要加入暫存區：`y` 加入、`n` 不加、`s` 再切小、`q` 離開 |
| `git restore --staged 檔案` | 把檔案移出暫存區，修改內容還在 |
| `git restore 檔案` | 放棄工作目錄的修改 ⚠️ 修改會消失 |
| `git rm --cached 檔案` | 停止追蹤這個檔案，但保留電腦上的檔案 |

紀錄：

---

## 6. 練習 3：別人 clone 下來是壞的

> 目的：理解「我的電腦能跑」不代表「別人也能跑」，學會送出前自己驗證。
> 這個練習需要一個可以編譯的小專案，屆時再加入，並設定 GitHub Actions（CI）在乾淨環境自動編譯。

步驟（練習時逐步填寫）：

1. 新增一個檔案並在其他地方引用它，但故意不 `git add` 這個新檔案。
2. 推上去開 PR，看 CI 失敗並讀懂錯誤訊息。
3. 另外 clone 一份到別的資料夾，假裝是同事，親自體驗失敗。
4. 補上檔案，看 CI 通過。
5. 學會用 `git worktree` 在推送前就發現問題。

關鍵指令：

| 指令 | 意義 |
|---|---|
| `git status --untracked-files=all` | 列出所有還沒被追蹤的檔案，用來發現忘了 add 的新檔案 |
| `git worktree add ..\verify HEAD` | 用目前的 commit 建立一份乾淨的副本，只包含已提交的檔案，效果像別人 clone 下來 |
| `git worktree remove ..\verify` | 刪除那份副本 |
| `git diff origin/develop...HEAD --check` | 檢查有沒有衝突標記或多餘的空白 |

紀錄：

---

## 7. 送出 PR 前的檢查清單

```
【開始開發前】
□ 確認這次的目標分支是哪一個
□ git fetch origin
□ git switch -c feature/xxx origin/<目標分支>

【提交時】
□ git status：沒有 local.properties、.idea、build 等不該提交的檔案
□ 用 git add -p 或指定檔案，不用 git add .
□ git diff --staged：確認要提交的內容

【送出 PR 前】
□ git fetch origin，並同步目標分支
□ git log --oneline origin/<目標分支>..HEAD：只有自己的 commit
□ git diff origin/<目標分支>...HEAD --stat：只有自己改的檔案
□ git status --untracked-files=all：沒有遺漏的新檔案
□ 用 worktree 乾淨編譯過

【開 PR 時】
□ 再確認一次目標分支（base branch）
□ 描述寫清楚：改了什麼、為什麼改、怎麼測試
```

---

## 8. 補救指令速查

| 狀況 | 指令 | 說明 |
|---|---|---|
| 剛 commit，訊息打錯或漏了檔案（還沒 push） | `git add 漏掉的檔案` → `git commit --amend` | 修改最後一個 commit |
| 檔案加進暫存區，但不想提交 | `git restore --staged 檔案` | 修改內容還在 |
| commit 到錯的分支（還沒 push） | `git switch 正確分支` → `git cherry-pick <SHA>` → 回到錯的分支 `git reset --hard HEAD~1` | 搬到正確的分支 |
| 已經 push 的 commit 要撤銷 | `git revert <SHA>` | 產生反向 commit，不改寫歷史，共用分支要用這個 |
| 不小心 reset 掉 commit | `git reflog` → `git branch rescue <SHA>` | reflog 記錄了 HEAD 移動過的所有位置 |
| 修改到一半，要先切去別的分支 | `git stash` → 處理完 → `git stash pop` | 暫時收起修改 |
| rebase 之後要重新推送自己的分支 | `git push --force-with-lease` | 比 `--force` 安全，遠端有別人的新提交時會拒絕 |

**原則：已經推到共用分支的東西，用 `revert`，不要用 `reset` + force push。**

---

## 9. 名詞解釋

| 名詞 | 意義 |
|---|---|
| repository（repo） | 儲存庫，存放專案和所有版本紀錄 |
| commit | 一次提交，也就是一個版本快照 |
| SHA | 每個 commit 的唯一識別碼（一串英數字） |
| branch | 分支，指向某個 commit 的指標 |
| HEAD | 目前所在的位置 |
| origin | 遠端儲存庫的慣用名稱 |
| upstream（上游分支） | 本機分支對應的遠端分支 |
| PR（Pull Request）／MR（Merge Request） | 請求把我的分支合併到目標分支，GitHub 叫 PR，GitLab 叫 MR |
| base branch／target branch | PR 的目標分支 |
| merge | 合併，保留兩邊的歷史並產生一個合併 commit |
| rebase | 把我的 commit 重新接到目標分支的最新位置，歷史變成一條直線 |
| conflict | 衝突，兩邊修改了同一個地方，Git 無法自動決定 |
| CI | 持續整合，每次推送或開 PR 時自動編譯和測試 |

---

## 10. 我的錯誤與心得紀錄

> 每次出錯都記下來：發生什麼事、原因、怎麼修、以後怎麼預防。

| 日期 | 發生什麼事 | 原因 | 怎麼修正 | 以後怎麼預防 |
|---|---|---|---|---|
| | | | | |
