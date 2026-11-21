# iOS 18 Launcher Pro — v0.1.0

Projeto inicial de launcher Android com interface inspirada no estilo iOS, sem usar marcas/recursos proprietários da Apple.

## O que já funciona
- Atividade selecionável como tela inicial padrão (HOME).
- Fundo em degradê, relógio visual e campo de pesquisa.
- Grade de aplicativos instalados, com ícones reais.
- Abrir aplicativos ao tocar.
- Layout adaptável à densidade e ao tamanho da tela.

## Limitações da versão 0.1
É um protótipo inicial. Ainda não inclui widgets reais, tela de bloqueio, painel de controle, notificações dinâmicas ou animações avançadas. O relógio superior é demonstrativo.

## Gerar APK sem computador
1. Crie um repositório público ou privado no GitHub.
2. Envie todos os arquivos desta pasta para a raiz do repositório.
3. Abra a aba **Actions** e execute **Build Android APK** (ou faça um commit na branch `main`).
4. Abra a execução concluída, entre em **Artifacts** e baixe `ios18-launcher-pro-debug`.
5. Extraia o ZIP e instale `app-debug.apk` no Android.
6. Ao pressionar Home, escolha **iOS 18 Launcher Pro** e selecione **Sempre**.

## Teste no Samsung Galaxy A14
A compilação usa `minSdk 23` e layout responsivo. O APK de debug é para testes, não é uma versão assinada para publicação na Play Store.

## Privacidade
A permissão QUERY_ALL_PACKAGES é usada para listar os aplicativos instalados e abrir os escolhidos. O launcher não envia essa lista para servidores.
