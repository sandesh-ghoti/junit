# To learn more about how to use Nix to configure your environment
# see: https://developers.google.com/idx/guides/customize-idx-env
{ pkgs, ... }: {
  # Which nixpkgs channel to use.
  channel = "stable-23.11"; # or "unstable"
  # Use https://search.nixos.org/packages to find packages
  packages = [
    pkgs.zulu17
    pkgs.maven
    pkgs.nodejs
    pkgs.minikube
    pkgs.kubectl
  ];

  # activate services
  services.docker.enable = true;
  services.mysql={
    enable=true;
    package = pkgs.mysql80;
  };
  # Sets environment variables in the workspace
  env = {
    PORT = "3000";
  };
  idx = {
    # Search for the extensions you want on https://open-vsx.org/ and use "publisher.id"
    extensions = [
      "vscjava.vscode-java-pack"
      "humao.rest-client"
      "redhat.fabric8-analytics"
      # "googlecloudtools.cloudcode"

    ];
    workspace = {
      # Runs when a workspace is first created with this `dev.nix` file
      onCreate = {
        install = "mvn clean install && mysqladmin -u root password \"root\"";
      };
      # Runs when a workspace is (re)started
      onStart = {
        # run-accounts = "cd accounts && mvn spring-boot:run";
        # run-cards = "cd cards && mvn spring-boot:run";
      };
    };
    # previews = {
    #   enable = false;
    #   previews = {
    #     web = {
    #       command = [
    #         "npm"
    #         "run"
    #         "start"
    #         "--"
    #         "--port"
    #         "$PORT"
    #         "--host"
    #         "0.0.0.0"
    #         "--disable-host-check"
    #       ];
    #       cwd = "frontend";
    #       manager = "web";
    #     };
    #   };
    # };
  };
}