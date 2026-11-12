# Pokédex

Aplicativo Android para explorar Pokémons via PokéAPI e montar uma equipe de favoritos salva localmente por treinador.


## API utilizada

- **Nome:** PokéAPI
- **Endereço base:** `https://pokeapi.co/api/v2/`
- **Endpoints utilizados:** `GET pokemon` (listagem) e `GET pokemon/{idOuNome}` (tipos, peso, altura e status base).

## Prints

*(Inserir aqui de 4 a 6 prints do emulador: login, cadastro, lista explorar, detalhe, equipe/favoritos e estado de erro)*

## Divisão de tarefas

| Integrante | O que fez |
|------------|-----------|
| Integrante 1 | Camadas `network` e `data` (Retrofit, PokéAPI, Room Database, autenticação com SHA-256 e `SessaoManager`). |
| Integrante 2 | Camadas `viewmodel` e `userinterface` (ViewModel, estados de UI, telas Compose e navegação type-safe). |

## O que aprendemos

- **Integrante 1:** Aprendemos a integrar Retrofit com coroutines e persistir dados e sessões locais por usuário usando Room e SharedPreferences.
- **Integrante 2:** Aprendemos a separar responsabilidades respeitando a Regra de Dependência e a construir navegação type-safe com barra inferior no Jetpack Compose.
