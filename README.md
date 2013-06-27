# CostLog
これはAndroidで動作するアプリケーションです。  
Android4.0から4.2での動作検証を行っています。

##プロジェクトの目的
DrawerLayoutのテスト
##プロジェクトの主旨
DrawerLayoutのレイアウトファイル内に２つのドロワーを入れて操作はできないかというテスト

## Runtime Environment
eclipseから作ったエミュレーター  
Android4.0.4を搭載したスマートフォン

## Develop Environment
windows7 proffesional 64bit  
jdk 1.6
eclipse 4.2 juno  
character code utf-8  
Android SDK Tools r22  
Android Platform-tools 17  
Android SDK Build-tools r22  
minSdkVersion="14"  
targetSdkVersion="17"  
compile with "17"  

## 現在の状態
開発中

## 今やっていること
右のドロワーが開いているときにドロワー外のタッチでドロワーを閉じる。

## 終わっていること
右左のドロワー開閉。android.id.homeを押した場合の挙動。

##確認されている問題点
右のドロワーが開いているときにドロワー外のタッチでドロワーを閉じれない。謎。  
右のドロワーが開いたときにアクションバーを変えたい。具体的には下に出すとか色を変えるとか。たぶんできる。  
コンテンツを選択したときに右のドロワーの内容も変えるべきか。選択可能にしておく。  