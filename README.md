# touhoumod - ゆっくり & 弾幕 Fabric MOD

Minecraft **1.21.1** / Fabric Loader **0.16.x** 系向けの Fabric MOD です。
「ゆっくり」風の丸い生き物と、東方Project的な「弾幕」(danmaku) の弾幕システムを追加し、
弾は追加のコアシェーダーでグロー(発光)表現されます。

## 入っている要素

- **ゆっくり (`touhoumod:yukkuri`)** — 平べったく丸いモブ。普段は徘徊しているだけですが、
  プレイヤーに攻撃されたり近づかれて敵対すると、弾幕(リング状の弾幕 → 一定時間後は
  回転する渦状弾幕)を放ってきます。`/summon touhoumod:yukkuri` でスポーンできます。
- **弾幕の弾 (`touhoumod:danmaku_bullet`)** — 重力の影響を受けない一定速度の弾。
  当たり判定・ダメージ処理を持つ独自エンティティで、見た目はカメラ常に正面を向く
  ビルボード板ポリゴンとして描画されます。
- **弾幕の杖 (`touhoumod:danmaku_rod`)** — プレイヤーが右クリックで扇状の弾幕を放てるアイテム。
  クリエイティブの「戦闘」タブに追加されます。
- **カスタムコアシェーダー (`assets/touhoumod/shaders/core/danmaku_bullet.*`)** — 弾幕の弾専用の
  加算合成 + パルス発光シェーダー。`CoreShaderRegistrationCallback` (Fabric API) で登録し、
  独自の `RenderLayer` から参照しています。

弾幕のパターン生成ロジックは `danmaku/DanmakuPattern.java` にまとまっており、
リング状・扇状(狙い撃ち)・渦巻き状の3種類を用意しています。東方Danmakufu的な
「発射数・半径・速度」の考え方を参考にしたシンプルなAPIです。

## テクスチャについて

同梱しているテクスチャ (`yukkuri.png` / `danmaku_bullet.png` / `danmaku_rod.png` / `icon.png`) は
このリポジトリ用にスクリプトで生成したオリジナルのプレースホルダー画像です。ZUN氏の東方Project
公式アートワークや、二次創作「ゆっくり」の既存イラスト・音声素材は一切含んでいません。
公開・配布する際は、ご自身の権利で使えるアートワークに差し替えることを推奨します。

## ビルド方法

```bash
./gradlew build   # または: gradle build (Gradle 8.x, JDK 21)
```

Gradle Wrapper のjarは同梱していません。初回は `gradle wrapper --gradle-version 8.10.2` などで
生成するか、お手元の Gradle (8.x系, JDK21) でそのままビルドしてください。

`gradle.properties` にバージョン一覧があります。特に以下は
[fabricmc.net/develop](https://fabricmc.net/develop) で最新の組み合わせを確認して
必要なら更新してください:

- `minecraft_version` (1.21.1)
- `yarn_mappings`
- `loader_version`
- `fabric_version` (Fabric API)
- `build.gradle` 内の Fabric Loom プラグインバージョン (`1.9.2` 指定)

## ⚠️ 既知のリスク / ビルド検証について

このプロジェクトは、**Minecraft/Yarn/Fabric の Maven リポジトリにネットワークアクセスできない
サンドボックス環境**で作成されました。そのため `./gradlew build` による実コンパイル確認は
行えていません。Java の構文自体は `javac` でパース検証済みですが、Minecraft側API
(Yarn マッピングの正確なメソッド名・シグネチャ) は執筆時点の知識に基づく最善の推測です。

とくに以下は 1.21.1 前後で名前が変わりやすい/確認しづらい箇所なので、コンパイルエラーが出た
場合はまずここを疑ってください:

1. **`ModRenderLayers.java` / `TouhouShaders.java`** — `RenderPhase.ShaderProgram`
   (シェーダー参照用の内部フェーズクラス名) は、マッピングによっては `RenderPhase.Shader`
   の場合があります。`net.minecraft.client.gl.ShaderProgram` (旧 `ShaderInstance`) の
   名前とセットで確認してください。
2. **`DanmakuBulletEntity.java`** — `LivingEntity#damage` の引数に `ServerWorld` が
   必要かどうか、`DamageSources#mobProjectile` の存在、`ProjectileUtil#getCollision` の
   シグネチャ。
3. **`YukkuriEntity.java`** — `HostileEntity.createHostileAttributes()` の名前、
   `HostileEntity#initGoals()` のオーバーライド。
4. **`DanmakuRodItem.java`** — `Item#use` の戻り値が `ActionResult` か
   `TypedActionResult<ItemStack>` か(1.21 で変更されたはずですが要確認)。
5. **`ModEntities.java` / `ModItems.java`** — `EntityType.Builder#build` /
   `Item.Settings#registryKey` に `RegistryKey` を渡す新しめのAPI形。

いずれも該当箇所にコード内コメントで注意点を残してあります。エラーメッセージを教えて
いただければ、該当箇所だけ素早く修正できます。

## テスト方法 (実機での確認手順)

1. `./gradlew runClient` でテスト用クライアントを起動。
2. クリエイティブでコマンド `/summon touhoumod:yukkuri` を実行。
3. ゆっくりを攻撃して敵対化させ、弾幕(リング状 → 200tick経過後は渦巻き状)を確認。
4. クリエイティブインベントリの「戦闘」タブから「弾幕の杖」を入手し、右クリックで
   扇状弾幕を発射。弾がグロー(発光)表示されることを確認。

(この環境ではMinecraftクライアントを起動できないため、上記手順はローカル環境での
確認をお願いします。)
