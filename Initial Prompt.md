# Initial Prompt
/superpowers:brainstorm analyze idea

ArcaneDesignSystem is repository for Claude Code (for LLMs)
I use ArcaneDesignSystem for fast prototyping.

ArcaneDesignSystem provides basic elements and additional modules for apps.
ArcaneDesignSystem contains much smaller set of Material 3.

# Goals
## limit LLM context
## have consitent UI in all apps, components work well together
## Generated Compose code
- looks very bad?
- UI makes you cry?
- Claude used wrong library version, again? - forget, ArcaneDesignSystem is mandatory for claude to use, Material 3 imports must be justified.

My use case: I need chat screens for new app "CV Agent".
My solution:
1. Crate new module in ArcaneDesignSystem, for chat app.
Light fast prototype new designs, elements, screens.
Interactive, with animations, previes, etc. on desktop app.
Wny Desktop, because build fast!
- Desktop - 10 sec on my mac
- Android - 2min
- iOS - 99% infinity build. *xCode tooling sucks*!

2. When module ready (chat as an example)
Chage in application is easy. Claude limited to library. Much less read file, and compactions. Smaller context.
Much faster development!

# Documentation
- I need skill? or waht? Claude ultrathink suggest.
