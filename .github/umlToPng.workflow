workflow "Generate PlantUML Images" {
  resolves = ["Generate UML"]
  on = "push"
}

action "Generate UML" {
  uses = "lokkju/github-action-plantuml@master"
  args = ["plantuml/use-case.puml"]
}